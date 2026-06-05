package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a WhatsApp session returned by the REST API.
 */
@Schema(name = "WhatsappSessionResponse", description = "A seller's WhatsApp channel session")
public record WhatsappSessionResource(
        @Schema(description = "Session unique identifier", example = "1")
        Long id,

        @Schema(description = "Identifier of the owning seller", example = "1")
        Long sellerId,

        @Schema(description = "WhatsApp phone number", example = "+51 999 888 777")
        String phone,

        @Schema(description = "Business display name", example = "Bodega El Huerto")
        String businessName,

        @Schema(description = "Connection status", example = "connected")
        SessionStatus status,

        @Schema(description = "Instant the session was connected", nullable = true)
        Instant connectedAt,

        @Schema(description = "Pairing QR payload", nullable = true)
        String qrCode
) {
}
