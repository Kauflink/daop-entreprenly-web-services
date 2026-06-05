package online.entreprenly.platform.sales.interfaces.rest.resources;

import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Resource received to register a new sale.
 *
 * <p>The server is the source of truth for the {@code total} and line subtotals, so
 * those values are recomputed regardless of what the client sends.</p>
 */
@Schema(name = "CreateSaleRequest", description = "Request to register a sale")
public record CreateSaleResource(
    @Schema(description = "Identifier of the seller that performed the sale", example = "1")
    @NotNull
    Long sellerId,

    @Schema(description = "Sale line items")
    @NotEmpty
    List<SaleItemResource> items,

    @Schema(description = "Payment method used", example = "CASH")
    PaymentMethod paymentMethod,

    @Schema(description = "Proof of payment", nullable = true)
    PaymentReceiptResource paymentReceipt,

    @Schema(description = "Lifecycle status", example = "COMPLETED", nullable = true)
    SaleStatus status
) {
}
