package online.entreprenly.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a stock alert returned by the REST API.
 */
@Schema(name = "StockAlertResponse", description = "A derived inventory stock alert")
public record StockAlertResource(
    @Schema(description = "Run-scoped stock alert identifier", example = "1")
    Long id,

    @Schema(description = "Identifier of the lot that triggered the alert (null when not lot-bound)",
            example = "3", nullable = true)
    Long lotId,

    @Schema(description = "Identifier of the product the alert refers to", example = "1")
    Long productId,

    @Schema(description = "Measurement type of the product", example = "unit", nullable = true)
    String productType,

    @Schema(description = "Product display name", example = "Coca-Cola 500ml")
    String productName,

    @Schema(description = "Alert type", example = "low_stock")
    String alertType,

    @Schema(description = "Alert severity", example = "warning")
    String severity,

    @Schema(description = "Human-readable alert message", example = "Coca-Cola 500ml is running low on stock")
    String message,

    @Schema(description = "Instant the alert was raised", example = "2026-06-03T15:04:05Z")
    Instant createdAt
) {
}
