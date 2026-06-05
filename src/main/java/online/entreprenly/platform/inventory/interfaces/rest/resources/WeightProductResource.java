package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing a weight product returned by the REST API.
 */
@Schema(name = "WeightProductResponse", description = "A product tracked and sold by weight")
public record WeightProductResource(
    @Schema(description = "Weight product unique identifier", example = "1")
    Long id,

    @Schema(description = "Product display name", example = "Red Apple")
    String name,

    @Schema(description = "Product description", example = "Fresh red apples sold by weight")
    String description,

    @Schema(description = "QR code identifying the product", example = "QR-WP-0001")
    String codeQR,

    @Schema(description = "Measurement type", example = "weight")
    String productType,

    @Schema(description = "Price per kilogram", example = "4.2")
    double pricePerKg
) {
}
