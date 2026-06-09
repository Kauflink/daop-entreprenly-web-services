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
 * <p>The bridge calls are guarded by a shared token ({@code X-Bridge-Token}) since the
 * bridge cannot present a user JWT.</p>
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

    @PostMapping("/qr")
    @Operation(summary = "Relay the latest pairing QR (from the bridge)")
    public ResponseEntity<Void> pushQr(@RequestHeader(value = "X-Bridge-Token", required = false) String token,
                                       @RequestBody BridgeQrResource resource) {
        if (isUnauthorized(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        bridgeState.setQr(resource.qr());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/status")
    @Operation(summary = "Report the bridge connection state (from the bridge)")
    public ResponseEntity<Void> pushStatus(@RequestHeader(value = "X-Bridge-Token", required = false) String token,
                                           @Valid @RequestBody BridgeStatusResource resource) {
        if (isUnauthorized(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        bridgeState.setConnected(resource.connected());
        bridgeState.setOwnerEmail(resource.ownerEmail());
        sessionCommandService.handle(new ReportBridgeConnectionCommand(
                resource.connected(), resource.phone(), resource.businessName(), resource.sellerId()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/qr")
    @Operation(summary = "Get the current pairing QR and link state (for the frontend)")
    public ResponseEntity<BridgeQrStateResource> getQrState() {
        return ResponseEntity.ok(new BridgeQrStateResource(bridgeState.getQr(), bridgeState.isConnected()));
    }

    private boolean isUnauthorized(String token) {
        return token == null || !bridgeToken.equals(token);
    }
}
