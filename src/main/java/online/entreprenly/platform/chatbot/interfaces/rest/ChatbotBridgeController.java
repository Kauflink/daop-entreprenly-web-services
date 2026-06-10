package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.WhatsappSessionCommandService;
import online.entreprenly.platform.chatbot.domain.model.commands.ReportBridgeConnectionCommand;
import online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp.WhatsAppBridgeState;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeQrResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeQrStateResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.BridgeStatusResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private final WhatsAppBridgeState bridgeState;
    private final WhatsappSessionCommandService sessionCommandService;
    private final String bridgeToken;

    public ChatbotBridgeController(WhatsAppBridgeState bridgeState,
                                   WhatsappSessionCommandService sessionCommandService,
                                   @Value("${chatbot.whatsapp.bridge-token:entreprenly-bridge-secret}") String bridgeToken) {
        this.bridgeState = bridgeState;
        this.sessionCommandService = sessionCommandService;
        this.bridgeToken = bridgeToken;
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
        sessionCommandService.handle(new ReportBridgeConnectionCommand(
                resource.connected(), resource.phone(), resource.businessName(), resource.sellerId()));
        return ResponseEntity.noContent().build();
    }

    /**
     * Called by the frontend every few seconds to get the current QR and connection state.
     * Returns the state for the authenticated seller only.
     */
    @GetMapping("/qr")
    @Operation(summary = "Get the current pairing QR and link state (for the frontend)")
    public ResponseEntity<BridgeQrStateResource> getQrState(Authentication authentication) {
        String email = (authentication != null) ? authentication.getName() : null;
        return ResponseEntity.ok(new BridgeQrStateResource(
                bridgeState.getQr(email),
                bridgeState.isConnected(email)));
    }

    private boolean isUnauthorized(String token) {
        return token == null || !bridgeToken.equals(token);
    }
}
