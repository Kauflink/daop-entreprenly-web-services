package online.entreprenly.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Resource representing a cash register returned by the REST API.
 */
@Schema(name = "CashRegisterResponse", description = "Daily cash register summary")
public record CashRegisterResource(
    @Schema(description = "Cash register unique identifier", example = "1")
    Long id,

    @Schema(description = "Business day this register belongs to", example = "2026-06-03")
    LocalDate date,

    @Schema(description = "Total cash takings", example = "120.5")
    double totalCash,

    @Schema(description = "Total digital takings", example = "80.0")
    double totalDigital,

    @Schema(description = "Number of sales registered for the day", example = "12")
    int saleCount
) {
}
