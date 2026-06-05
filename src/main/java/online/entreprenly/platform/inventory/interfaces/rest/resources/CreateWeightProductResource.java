package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Resource received to create a weight product.
 */
@Schema(name = "CreateWeightProductRequest", description = "Request to create a weight product")
public record CreateWeightProductResource(
    @Schema(description = "Product display name", example = "Red Apple")
    @NotBlank
    String name,

    @Schema(description = "Product description", example = "Fresh red apples sold by weight")
    String description,

    @Schema(description = "QR code identifying the product", example = "QR-WP-0001")
    String codeQR,

    @Schema(description = "Price per kilogram", example = "4.2")
    @PositiveOrZero
    double pricePerKg
) {
}
