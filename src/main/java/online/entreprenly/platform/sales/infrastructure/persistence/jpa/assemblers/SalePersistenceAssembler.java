package online.entreprenly.platform.sales.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentReceipt;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.embeddables.PaymentReceiptEmbeddable;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.embeddables.SaleItemEmbeddable;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities.SalePersistenceEntity;

import java.util.List;

/**
 * Static assembler between sale domain and persistence representations.
 */
public final class SalePersistenceAssembler {

    private SalePersistenceAssembler() {
    }

    public static Sale toDomainFromPersistence(SalePersistenceEntity entity) {
        if (entity == null) return null;
        List<SaleItem> items = entity.getItems().stream()
                .map(item -> new SaleItem(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getWeightKg(),
                        item.getUnitPrice(),
                        item.getSubtotal()))
                .toList();
        PaymentReceipt paymentReceipt = toPaymentReceiptDomain(entity.getPaymentReceipt());
        var sale = new Sale();
        sale.restoreState(
                entity.getId(),
                entity.getOwnerEmail(),
                entity.getSellerId(),
                items,
                entity.getTotal(),
                entity.getPaymentMethod(),
                paymentReceipt,
                entity.getStatus(),
                entity.getSaleCreatedAt(),
                entity.getCompletedAt());
        return sale;
    }

    public static SalePersistenceEntity toPersistenceFromDomain(Sale sale) {
        if (sale == null) return null;
        var entity = new SalePersistenceEntity();
        if (sale.getId() != null) {
            entity.setId(sale.getId());
        }
        entity.setOwnerEmail(sale.getOwnerEmail());
        entity.setSellerId(sale.getSellerId());
        entity.setItems(sale.getItems().stream()
                .map(item -> new SaleItemEmbeddable(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.weightKg(),
                        item.unitPrice(),
                        item.subtotal()))
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new)));
        entity.setTotal(sale.getTotal());
        entity.setPaymentMethod(sale.getPaymentMethod());
        entity.setPaymentReceipt(toPaymentReceiptEmbeddable(sale.getPaymentReceipt()));
        entity.setStatus(sale.getStatus());
        entity.setSaleCreatedAt(sale.getCreatedAt());
        entity.setCompletedAt(sale.getCompletedAt());
        return entity;
    }

    private static PaymentReceipt toPaymentReceiptDomain(PaymentReceiptEmbeddable embeddable) {
        if (embeddable == null || embeddable.getMethod() == null) return null;
        return new PaymentReceipt(
                embeddable.getMethod(),
                embeddable.getTransactionCode(),
                embeddable.getAmount() == null ? 0d : embeddable.getAmount(),
                embeddable.getConfirmedAt());
    }

    private static PaymentReceiptEmbeddable toPaymentReceiptEmbeddable(PaymentReceipt receipt) {
        if (receipt == null) return null;
        return new PaymentReceiptEmbeddable(
                receipt.method(),
                receipt.transactionCode(),
                receipt.amount(),
                receipt.confirmedAt());
    }
}
