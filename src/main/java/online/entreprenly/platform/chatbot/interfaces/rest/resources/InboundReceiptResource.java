package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


@Schema(name = "InboundWhatsAppReceipt", description = "An inbound WhatsApp payment receipt")
public record InboundReceiptResource(
        @NotBlank
        @Schema(description = "Client phone number", example = "+51 987 654 321")
        String fromPhone,

        @Schema(description = "Seller account email reported by the bridge", nullable = true)
        String ownerEmail,

        @NotBlank
        @Schema(description = "Receipt image as a data URL", example = "data:image/jpeg;base64,...")
        String image
) {
}
