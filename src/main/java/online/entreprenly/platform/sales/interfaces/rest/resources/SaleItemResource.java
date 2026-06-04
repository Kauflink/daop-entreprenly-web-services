package online.entreprenly.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing a single sale line item.
 */
@Schema(name = "SaleItem", description = "A line item within a sale")
public record SaleItemResource(
    @Schema(description = "Product identifier", example = "1")
    Long productId,

    @Schema(description = "Product display name", example = "Apple")
    String productName,

    @Schema(description = "Number of units sold (null when sold by weight)", example = "3", nullable = true)
    Integer quantity,

    @Schema(description = "Weight sold in kilograms (null when sold by unit)", example = "1.25", nullable = true)
    Double weightKg,

    @Schema(description = "Price per unit or per kilogram", example = "2.5")
    double unitPrice,

    @Schema(description = "Computed line subtotal", example = "7.5")
    double subtotal
) {
}
