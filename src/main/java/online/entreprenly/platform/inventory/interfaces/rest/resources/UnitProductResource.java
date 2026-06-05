package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing a unit product returned by the REST API.
 */
@Schema(name = "UnitProductResponse", description = "A product tracked and sold per unit")
public record UnitProductResource(
    @Schema(description = "Unit product unique identifier", example = "1")
    Long id,

    @Schema(description = "Product display name", example = "Coca-Cola 500ml")
    String name,

    @Schema(description = "Product description", example = "Bottled soft drink")
    String description,

    @Schema(description = "QR code identifying the product", example = "QR-UP-0001")
    String codeQR,

    @Schema(description = "Measurement type", example = "unit")
    String productType,

    @Schema(description = "Unit price", example = "3.5")
    double price,

    @Schema(description = "Per-unit weight in grams", example = "500.0")
    double weightGrams,

    @Schema(description = "Product brand", example = "Coca-Cola")
    String brand
) {
}
