package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.WhatsappSessionCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.domain.model.commands.ReportBridgeConnectionCommand;
import online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp.WhatsAppBridgeState;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeQrResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeQrStateResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeStatusResource;
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

/**
 * Endpoints used by the WhatsApp bridge (whatsapp-web.js) to relay the real pairing
 * QR and connection state, and consumed by the frontend to render that QR and unlock
 * the dashboard once the channel is connected.
 *
 * <p>Bridge → backend calls are guarded by {@code X-Bridge-Token}.
 * Frontend → backend calls are guarded by the regular JWT.</p>
 *
 * <p>Multi-tenant: each seller's QR and connected state are stored separately,
 * keyed by the seller's e-mail address.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/chatbot/whatsapp/bridge", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chatbot - WhatsApp Bridge", description = "Real WhatsApp pairing relay")
public class ChatbotBridgeController {

    private static final Logger log = LoggerFactory.getLogger(ChatbotBridgeController.class);

    private final WhatsAppBridgeState bridgeState;
    private final WhatsappSessionCommandService sessionCommandService;
    private final SellerEmailResolver sellerEmailResolver;
    private final String bridgeToken;
    private final String bridgeBaseUrl;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();

    public ChatbotBridgeController(WhatsAppBridgeState bridgeState,
                                   WhatsappSessionCommandService sessionCommandService,
                                   SellerEmailResolver sellerEmailResolver,
                                   @Value("${chatbot.whatsapp.bridge-token:entreprenly-bridge-secret}") String bridgeToken,
                                   @Value("${chatbot.whatsapp.bridge-base-url:}") String bridgeBaseUrl) {
        this.bridgeState = bridgeState;
        this.sessionCommandService = sessionCommandService;
        this.sellerEmailResolver = sellerEmailResolver;
        this.bridgeToken = bridgeToken;
        this.bridgeBaseUrl = bridgeBaseUrl;
    }

    /** Called by the bridge when a new pairing QR is generated for a seller. */
    @PostMapping("/qr")
    @Operation(summary = "Relay the latest pairing QR (from the bridge)")
    public ResponseEntity<Void> pushQr(
            @RequestHeader(value = "X-Bridge-Token", required = false) String token,
            @RequestBody BridgeQrResource resource) {
        if (isUnauthorized(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        bridgeState.setQr(resource.ownerEmail(), resource.qr());
        return ResponseEntity.noContent().build();
    }

    /** Called by the bridge when a seller's WhatsApp connects or disconnects. */
    @PostMapping("/status")
    @Operation(summary = "Report the bridge connection state (from the bridge)")
    public ResponseEntity<Void> pushStatus(
            @RequestHeader(value = "X-Bridge-Token", required = false) String token,
            @Valid @RequestBody BridgeStatusResource resource) {
        if (isUnauthorized(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        bridgeState.setConnected(resource.ownerEmail(), resource.connected());
        // The bridge only knows the seller's email, so it sends a placeholder sellerId.
        // Resolve the real account id from the email so the session — and every
        // conversation derived from it — carries a sellerId the IAM context can map
        // back (needed to deliver outbound messages and deduct stock).
        var sellerId = sellerEmailResolver.resolveSellerId(resource.ownerEmail())
                .orElse(resource.sellerId());
        sessionCommandService.handle(new ReportBridgeConnectionCommand(
                resource.connected(), resource.phone(), resource.businessName(), sellerId));
        return ResponseEntity.noContent().build();
    }

    /**
     * Called by the frontend every 5 s to get the current QR and connection state.
     * Returns the state for the authenticated seller only.
     * If no session is active yet, triggers the bridge to start one (fire-and-forget).
     */
    @GetMapping("/qr")
    @Operation(summary = "Get the current pairing QR and link state (for the frontend)")
    public ResponseEntity<BridgeQrStateResource> getQrState(Authentication authentication) {
        String email = (authentication != null) ? authentication.getName() : null;
        boolean connected = bridgeState.isConnected(email);
        String qr = bridgeState.getQr(email);
        if (!connected && qr == null && email != null) {
            triggerBridgeSession(email);
        }
        return ResponseEntity.ok(new BridgeQrStateResource(qr, connected));
    }

    /** Fire-and-forget call to the bridge to start a WhatsApp session for a seller. */
    private void triggerBridgeSession(String email) {
        if (bridgeBaseUrl == null || bridgeBaseUrl.isBlank()) return;
        try {
            var sellerId = sellerEmailResolver.resolveSellerId(email).orElse(1L);
            var url = URI.create(bridgeBaseUrl + "/qr?email=" +
                    java.net.URLEncoder.encode(email, java.nio.charset.StandardCharsets.UTF_8) +
                    "&sellerId=" + sellerId);
            var request = HttpRequest.newBuilder(url)
                    .GET()
                    .timeout(Duration.ofSeconds(5))
                    .build();
            http.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                    .exceptionally(ex -> { log.debug("[bridge] session trigger failed for {}: {}", email, ex.getMessage()); return null; });
        } catch (Exception ex) {
            log.debug("[bridge] could not trigger session for {}: {}", email, ex.getMessage());
        }
    }

    private boolean isUnauthorized(String token) {
        return token == null || !bridgeToken.equals(token);
    }
}
