package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing a chat order line item.
 */
@Schema(name = "OrderItem", description = "A chat order line item")
public record OrderItemResource(
        @Schema(description = "Product display name", example = "Coca Cola 500ml")
        String productName,

        @Schema(description = "Units requested", example = "3")
        int quantity,

        @Schema(description = "Price per unit", example = "2.5")
        double unitPrice
) {
}
