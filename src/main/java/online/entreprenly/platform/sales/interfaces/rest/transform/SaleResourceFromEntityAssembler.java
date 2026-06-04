package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentReceipt;
import online.entreprenly.platform.sales.interfaces.rest.resources.PaymentReceiptResource;
import online.entreprenly.platform.sales.interfaces.rest.resources.SaleItemResource;
import online.entreprenly.platform.sales.interfaces.rest.resources.SaleResource;

import java.util.List;

/**
 * Assembler that converts {@link Sale} aggregates into {@link SaleResource} objects.
 */
public class SaleResourceFromEntityAssembler {

    public static SaleResource toResourceFromEntity(Sale sale) {
        List<SaleItemResource> items = sale.getItems().stream()
                .map(item -> new SaleItemResource(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.weightKg(),
                        item.unitPrice(),
                        item.subtotal()))
                .toList();
        return new SaleResource(
                sale.getId(),
                sale.getSellerId(),
                items,
                sale.getTotal(),
                sale.getPaymentMethod(),
                toPaymentReceiptResource(sale.getPaymentReceipt()),
                sale.getStatus(),
                sale.getCreatedAt(),
                sale.getCompletedAt());
    }

    private static PaymentReceiptResource toPaymentReceiptResource(PaymentReceipt receipt) {
        if (receipt == null) return null;
        return new PaymentReceiptResource(
                receipt.method(),
                receipt.transactionCode(),
                receipt.amount(),
                receipt.confirmedAt());
    }
}
