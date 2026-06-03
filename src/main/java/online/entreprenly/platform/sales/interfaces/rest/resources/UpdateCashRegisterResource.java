package online.entreprenly.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource received to update the running totals of a cash register.
 */
@Schema(name = "UpdateCashRegisterRequest", description = "Request to update a cash register")
public record UpdateCashRegisterResource(
    @Schema(description = "New cash total", example = "120.5")
    double totalCash,

    @Schema(description = "New digital total", example = "80.0")
    double totalDigital,

    @Schema(description = "New number of sales registered for the day", example = "12")
    int saleCount
) {
}
