package online.entreprenly.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Sellable product exposed to the point-of-sale view, sourced from the Inventory catalog
 * with its currently available stock already computed by the Inventory bounded context.
 */
@Schema(name = "SalesProduct", description = "A product available to sell, with its computed stock")
public record SalesProductResource(
    @Schema(description = "Product display name", example = "Manzana")
    String name,

    @Schema(description = "Unit price for unit products, or price per kilogram for weight products", example = "2.5")
    double price,

    @Schema(description = "Whether the product is sold by weight (kilograms) instead of by unit", example = "false")
    boolean byWeight,

    @Schema(description = "Currently available stock (units for unit products, kilograms for weight products)", example = "12")
    double stock
) {
}
