package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.WhatsappSessionCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.domain.model.commands.ReportBridgeConnectionCommand;
import online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp.WhatsAppBridgeState;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeQrResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeQrStateResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeStatusResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


@RestController
@RequestMapping(value = "/api/v1/chatbot/whatsapp/bridge", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chatbot - WhatsApp Bridge", description = "Real WhatsApp pairing relay")
public class ChatbotBridgeController {

    private static final Logger log = LoggerFactory.getLogger(ChatbotBridgeController.class);

    private final WhatsAppBridgeState bridgeState;
    private final WhatsappSessionCommandService sessionCommandService;
    private final ChatbotSubscriptionGuard subscriptionGuard;
    private final SellerEmailResolver sellerEmailResolver;
    private final String bridgeToken;
    private final String bridgeBaseUrl;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatbotBridgeController(WhatsAppBridgeState bridgeState,
                                   WhatsappSessionCommandService sessionCommandService,
                                   ChatbotSubscriptionGuard subscriptionGuard,
                                   SellerEmailResolver sellerEmailResolver,
                                   @Value("${chatbot.whatsapp.bridge-token:entreprenly-bridge-secret}") String bridgeToken,
                                   @Value("${chatbot.whatsapp.bridge-base-url:}") String bridgeBaseUrl) {
        this.bridgeState = bridgeState;
        this.sessionCommandService = sessionCommandService;
        this.subscriptionGuard = subscriptionGuard;
        this.sellerEmailResolver = sellerEmailResolver;
        this.bridgeToken = bridgeToken;
        this.bridgeBaseUrl = bridgeBaseUrl;
    }

    
    @PostMapping("/qr")
    @Operation(summary = "Relay the latest pairing QR (from the bridge)",
            description = "Receives the latest WhatsApp pairing QR pushed by the bridge process.")
    public ResponseEntity<Void> pushQr(
            @RequestHeader(value = "X-Bridge-Token", required = false) String token,
            @RequestBody BridgeQrResource resource) {
        if (isUnauthorized(token) || !subscriptionGuard.canAccessOwner(resource.ownerEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        bridgeState.setQr(resource.ownerEmail(), resource.qr());
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping("/status")
    @Operation(summary = "Report the bridge connection state (from the bridge)",
            description = "Receives connection state updates pushed by the bridge process.")
    public ResponseEntity<Void> pushStatus(
            @RequestHeader(value = "X-Bridge-Token", required = false) String token,
            @Valid @RequestBody BridgeStatusResource resource) {
        if (isUnauthorized(token) || !subscriptionGuard.canAccessOwner(resource.ownerEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        bridgeState.setConnected(resource.ownerEmail(), resource.connected());
        var sellerId = sellerEmailResolver.resolveSellerId(resource.ownerEmail())
                .orElse(resource.sellerId());
        sessionCommandService.handle(new ReportBridgeConnectionCommand(
                resource.connected(), resource.phone(), resource.businessName(), sellerId));
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/qr")
    @Operation(summary = "Get the current pairing QR and link state (for the frontend)",
            description = "Returns the current pairing QR and link state for the frontend to display.")
    public ResponseEntity<BridgeQrStateResource> getQrState(Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = authentication.getName();
        boolean connected = bridgeState.isConnected(email);
        String qr = bridgeState.getQr(email);
        if (!connected && qr == null) {
            triggerBridgeSession(email);
        }
        return ResponseEntity.ok(new BridgeQrStateResource(qr, connected));
    }

    @DeleteMapping("/session")
    @Operation(summary = "Disconnect the WhatsApp session (from the frontend)",
            description = "Clears the current WhatsApp session and notifies the bridge to disconnect.")
    public ResponseEntity<Void> disconnectSession(Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = authentication.getName();
        bridgeState.clear(email);
        notifyBridgeDisconnect(email);
        return ResponseEntity.noContent().build();
    }

    private void notifyBridgeDisconnect(String email) {
        if (bridgeBaseUrl == null || bridgeBaseUrl.isBlank()) return;
        try {
            var url = URI.create(bridgeBaseUrl + "/disconnect?email="
                    + java.net.URLEncoder.encode(email, java.nio.charset.StandardCharsets.UTF_8));
            var request = HttpRequest.newBuilder(url)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("X-Bridge-Token", bridgeToken)
                    .timeout(Duration.ofSeconds(5))
                    .build();
            http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .exceptionally(ex -> {
                        log.debug("[bridge] disconnect notify failed for {}: {}", email, ex.getMessage());
                        return null;
                    });
        } catch (Exception ex) {
            log.debug("[bridge] could not notify disconnect for {}: {}", email, ex.getMessage());
        }
    }

    private void triggerBridgeSession(String email) {
        if (bridgeBaseUrl == null || bridgeBaseUrl.isBlank()) return;
        try {
            var sellerId = sellerEmailResolver.resolveSellerId(email).orElse(1L);
            var url = URI.create(bridgeBaseUrl + "/qr?email="
                    + java.net.URLEncoder.encode(email, java.nio.charset.StandardCharsets.UTF_8)
                    + "&sellerId=" + sellerId);
            var request = HttpRequest.newBuilder(url)
                    .GET()
                    .timeout(Duration.ofSeconds(5))
                    .build();
            http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> syncStateFromBridge(email, response))
                    .exceptionally(ex -> {
                        log.debug("[bridge] session trigger failed for {}: {}", email, ex.getMessage());
                        return null;
                    });
        } catch (Exception ex) {
            log.debug("[bridge] could not trigger session for {}: {}", email, ex.getMessage());
        }
    }

    
    private void syncStateFromBridge(String email, HttpResponse<String> response) {
        if (response.statusCode() / 100 != 2 || response.body() == null || response.body().isBlank()) {
            return;
        }
        try {
            JsonNode body = objectMapper.readTree(response.body());
            if (body.path("connected").asBoolean(false)) {
                bridgeState.setConnected(email, true);
            } else if (body.hasNonNull("qr")) {
                bridgeState.setQr(email, body.get("qr").asText());
            }
        } catch (Exception ex) {
            log.debug("[bridge] could not parse session state for {}: {}", email, ex.getMessage());
        }
    }

    private boolean isUnauthorized(String token) {
        return token == null || !bridgeToken.equals(token);
    }
}