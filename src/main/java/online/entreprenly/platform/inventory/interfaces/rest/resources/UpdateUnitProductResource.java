package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Resource received to update a unit product.
 */
@Schema(name = "UpdateUnitProductRequest", description = "Request to update a unit product")
public record UpdateUnitProductResource(
    @Schema(description = "Product display name", example = "Coca-Cola 500ml")
    @NotBlank
    String name,

    @Schema(description = "Product description", example = "Bottled soft drink")
    String description,

    @Schema(description = "QR code identifying the product", example = "QR-UP-0001")
    String codeQR,

    @Schema(description = "Unit price", example = "3.5")
    @PositiveOrZero
    double price,

    @Schema(description = "Per-unit weight in grams", example = "500.0")
    @PositiveOrZero
    double weightGrams,

    @Schema(description = "Product brand", example = "Coca-Cola")
    String brand
) {
}
