package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response exposing the current pairing QR and link state to the frontend.
 */
@Schema(name = "BridgeQrState", description = "Current WhatsApp pairing QR and link state")
public record BridgeQrStateResource(
        @Schema(description = "Raw QR payload to render (null when already connected)", nullable = true)
        String qr,

        @Schema(description = "Whether the channel is connected", example = "false")
        boolean connected
) {
}
