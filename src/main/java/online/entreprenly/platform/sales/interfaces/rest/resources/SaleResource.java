package online.entreprenly.platform.sales.interfaces.rest.resources;

import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Resource representing a sale returned by the REST API.
 */
@Schema(name = "SaleResponse", description = "A registered sale")
public record SaleResource(
    @Schema(description = "Sale unique identifier", example = "1")
    Long id,

    @Schema(description = "Identifier of the seller that performed the sale", example = "1")
    Long sellerId,

    @Schema(description = "Sale line items")
    List<SaleItemResource> items,

    @Schema(description = "Authoritative sale total", example = "7.5")
    double total,

    @Schema(description = "Payment method used", example = "CASH")
    PaymentMethod paymentMethod,

    @Schema(description = "Proof of payment", nullable = true)
    PaymentReceiptResource paymentReceipt,

    @Schema(description = "Lifecycle status", example = "COMPLETED")
    SaleStatus status,

    @Schema(description = "Instant the sale was created", example = "2026-06-03T15:04:05Z")
    Instant createdAt,

    @Schema(description = "Instant the sale was completed", example = "2026-06-03T15:04:05Z", nullable = true)
    Instant completedAt
) {
}
