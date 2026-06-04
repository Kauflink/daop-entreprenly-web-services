package online.entreprenly.platform.sales.interfaces.rest.resources;

import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing the proof of payment of a sale.
 */
@Schema(name = "PaymentReceipt", description = "Proof of payment attached to a sale")
public record PaymentReceiptResource(
    @Schema(description = "Payment method used", example = "YAPE")
    PaymentMethod method,

    @Schema(description = "External transaction code", example = "00123456")
    String transactionCode,

    @Schema(description = "Amount paid", example = "7.5")
    double amount,

    @Schema(description = "Instant the payment was confirmed", example = "2026-06-03T15:04:05Z")
    Instant confirmedAt
) {
}
