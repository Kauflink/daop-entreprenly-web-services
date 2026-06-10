package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Simplified inbound WhatsApp webhook payload.
 *
 * <p>Models the minimal fields the chatbot needs to route and answer a message.
 * A real WhatsApp Cloud API adapter would translate the provider payload into this
 * shape before invoking the application service.</p>
 */
@Schema(name = "InboundWhatsAppMessage", description = "An inbound WhatsApp message")
public record InboundWhatsAppMessageResource(
        @NotBlank
        @Schema(description = "Client phone number", example = "+51 987 654 321")
        String fromPhone,

        @Schema(description = "Client display name", example = "Andrea Torres", nullable = true)
        String clientName,

        @NotBlank
        @Schema(description = "Message text", example = "Hola, quiero hacer un pedido")
        String content,

        @Schema(description = "Seller account email reported by the bridge (resolves the catalog)",
                example = "vendedor@entreprenly.online", nullable = true)
        String ownerEmail
) {
}
