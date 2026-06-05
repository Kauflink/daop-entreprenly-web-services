package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a lot in the combined (unit + weight) read view.
 */
@Schema(name = "LotResponse", description = "A stock batch in the combined lot view")
public record LotResource(
    @Schema(description = "Lot unique identifier (scoped to its lot type)", example = "1")
    Long id,

    @Schema(description = "Identifier of the product this lot belongs to", example = "1")
    Long productId,

    @Schema(description = "QR code identifying the lot", example = "QR-UL-0001")
    String codeQR,

    @Schema(description = "Date the lot entered stock", example = "2026-06-03T00:00:00Z")
    Instant entryDate,

    @Schema(description = "Measurement type", example = "unit")
    String lotType
) {
}
