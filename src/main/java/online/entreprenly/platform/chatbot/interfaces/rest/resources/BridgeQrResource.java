package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request body carrying the latest WhatsApp pairing QR pushed by the bridge.
 */
@Schema(name = "BridgeQrRequest", description = "Latest WhatsApp pairing QR")
public record BridgeQrResource(
        @Schema(description = "Seller e-mail that owns this session", example = "seller@example.com")
        String ownerEmail,

        @Schema(description = "Raw QR payload to render", example = "2@abc...")
        String qr
) {
}
