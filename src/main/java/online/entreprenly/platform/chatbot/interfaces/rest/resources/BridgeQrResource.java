package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "BridgeQrRequest", description = "Latest WhatsApp pairing QR")
public record BridgeQrResource(
        @Schema(description = "Seller e-mail that owns this session", example = "seller@example.com")
        String ownerEmail,

        @Schema(description = "Raw QR payload to render", example = "2@abc...")
        String qr
) {
}
