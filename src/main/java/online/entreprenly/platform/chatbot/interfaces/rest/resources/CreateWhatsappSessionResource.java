package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body to register a new WhatsApp session.
 */
@Schema(name = "CreateWhatsappSessionRequest", description = "Data required to register a WhatsApp session")
public record CreateWhatsappSessionResource(
        @NotNull
        @Schema(description = "Identifier of the owning seller", example = "1")
        Long sellerId,

        @NotBlank
        @Schema(description = "WhatsApp phone number", example = "+51 999 888 777")
        String phone,

        @Schema(description = "Business display name", example = "Bodega El Huerto")
        String businessName,

        @Schema(description = "Pairing QR payload", nullable = true)
        String qrCode
) {
}
