package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

/**
 * Resource received to update a unit lot.
 */
@Schema(name = "UpdateUnitLotRequest", description = "Request to update a unit lot")
public record UpdateUnitLotResource(
    @Schema(description = "Identifier of the unit product this lot belongs to", example = "1")
    @NotNull
    Long productId,

    @Schema(description = "QR code identifying the lot", example = "QR-UL-0001")
    String codeQR,

    @Schema(description = "Date the lot entered stock", example = "2026-06-03T00:00:00Z")
    Instant entryDate,

    @Schema(description = "Number of units in the lot", example = "120")
    @PositiveOrZero
    int quantity,

    @Schema(description = "Lot expiry date", example = "2026-09-03T00:00:00Z")
    Instant expiryDate
) {
}
