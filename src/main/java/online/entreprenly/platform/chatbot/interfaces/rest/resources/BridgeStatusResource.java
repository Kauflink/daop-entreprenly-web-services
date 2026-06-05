package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body reporting the WhatsApp bridge connection state.
 */
@Schema(name = "BridgeStatusRequest", description = "WhatsApp bridge connection report")
public record BridgeStatusResource(
        @Schema(description = "Whether the bridge is linked to WhatsApp", example = "true")
        boolean connected,

        @NotBlank
        @Schema(description = "Linked WhatsApp phone number", example = "+51 999 888 777")
        String phone,

        @Schema(description = "Business display name", example = "Bodega El Huerto")
        String businessName,

        @Schema(description = "Owning seller id", example = "1")
        Long sellerId
) {
}
