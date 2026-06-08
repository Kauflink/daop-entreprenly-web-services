package online.entreprenly.platform.sales.domain.model.commands;

import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentReceipt;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleStatus;

import java.util.List;

/**
 * Command to register a new sale.
 *
 * @param ownerEmail     the authenticated account that owns the sale
 * @param sellerId       the seller that performed the sale
 * @param items          the sale line items
 * @param paymentMethod  the payment method used
 * @param paymentReceipt the proof of payment (nullable until payment is confirmed)
 * @param status         the lifecycle status to register the sale with (nullable; defaults applied)
 */
public record CreateSaleCommand(String ownerEmail, Long sellerId, List<SaleItem> items, PaymentMethod paymentMethod,
                                PaymentReceipt paymentReceipt, SaleStatus status) {
}
