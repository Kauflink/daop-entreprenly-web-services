package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentReceipt;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.sales.interfaces.rest.resources.CreateSaleResource;
import online.entreprenly.platform.sales.interfaces.rest.resources.PaymentReceiptResource;

import java.time.Instant;
import java.util.List;

/**
 * Assembler that translates {@link CreateSaleResource} into {@link CreateSaleCommand}.
 */
public class CreateSaleCommandFromResourceAssembler {

    public static CreateSaleCommand toCommandFromResource(String ownerEmail, CreateSaleResource resource) {
        List<SaleItem> items = resource.items() == null ? List.of() : resource.items().stream()
                .map(item -> SaleItem.of(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.weightKg(),
                        item.unitPrice()))
                .toList();
        PaymentReceipt paymentReceipt = toPaymentReceipt(resource.paymentReceipt());
        return new CreateSaleCommand(
                ownerEmail,
                resource.sellerId(),
                items,
                resource.paymentMethod(),
                paymentReceipt,
                resource.status());
    }

    private static PaymentReceipt toPaymentReceipt(PaymentReceiptResource resource) {
        if (resource == null) return null;
        var confirmedAt = resource.confirmedAt() == null ? Instant.now() : resource.confirmedAt();
        return new PaymentReceipt(resource.method(), resource.transactionCode(), resource.amount(), confirmedAt);
    }
}
