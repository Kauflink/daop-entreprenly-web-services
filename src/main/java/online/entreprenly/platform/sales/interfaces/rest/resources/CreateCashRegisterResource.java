package online.entreprenly.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Resource received to open a cash register for a business day.
 */
@Schema(name = "CreateCashRegisterRequest", description = "Request to open a cash register")
public record CreateCashRegisterResource(
    @Schema(description = "Business day this register belongs to", example = "2026-06-03")
    @NotNull
    LocalDate date,

    @Schema(description = "Initial cash total", example = "0.0")
    double totalCash,

    @Schema(description = "Initial digital total", example = "0.0")
    double totalDigital
) {
}
