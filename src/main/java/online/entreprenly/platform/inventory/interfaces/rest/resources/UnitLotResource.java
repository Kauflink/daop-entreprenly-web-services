package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a unit lot returned by the REST API.
 */
@Schema(name = "UnitLotResponse", description = "A stock batch of a per-unit product")
public record UnitLotResource(
    @Schema(description = "Unit lot unique identifier", example = "1")
    Long id,

    @Schema(description = "Identifier of the unit product this lot belongs to", example = "1")
    Long productId,

    @Schema(description = "QR code identifying the lot", example = "QR-UL-0001")
    String codeQR,

    @Schema(description = "Date the lot entered stock", example = "2026-06-03T00:00:00Z")
    Instant entryDate,

    @Schema(description = "Measurement type", example = "unit")
    String lotType,

    @Schema(description = "Number of units in the lot", example = "120")
    int quantity,

    @Schema(description = "Lot expiry date", example = "2026-09-03T00:00:00Z")
    Instant expiryDate
) {
}
