package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a weight lot returned by the REST API.
 */
@Schema(name = "WeightLotResponse", description = "A stock batch of a by-weight product")
public record WeightLotResource(
    @Schema(description = "Weight lot unique identifier", example = "1")
    Long id,

    @Schema(description = "Identifier of the weight product this lot belongs to", example = "1")
    Long productId,

    @Schema(description = "QR code identifying the lot", example = "QR-WL-0001")
    String codeQR,

    @Schema(description = "Date the lot entered stock", example = "2026-06-03T00:00:00Z")
    Instant entryDate,

    @Schema(description = "Measurement type", example = "weight")
    String lotType,

    @Schema(description = "Quantity in kilograms in the lot", example = "25.5")
    double quantityKg
) {
}
