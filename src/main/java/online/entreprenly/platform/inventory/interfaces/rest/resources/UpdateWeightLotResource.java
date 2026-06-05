package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

/**
 * Resource received to update a weight lot.
 */
@Schema(name = "UpdateWeightLotRequest", description = "Request to update a weight lot")
public record UpdateWeightLotResource(
    @Schema(description = "Identifier of the weight product this lot belongs to", example = "1")
    @NotNull
    Long productId,

    @Schema(description = "QR code identifying the lot", example = "QR-WL-0001")
    String codeQR,

    @Schema(description = "Date the lot entered stock", example = "2026-06-03T00:00:00Z")
    Instant entryDate,

    @Schema(description = "Quantity in kilograms in the lot", example = "25.5")
    @PositiveOrZero
    double quantityKg
) {
}
