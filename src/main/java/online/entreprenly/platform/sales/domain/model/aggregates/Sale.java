package online.entreprenly.platform.sales.domain.model.aggregates;

import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentReceipt;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleStatus;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Sale aggregate root.
 *
 * <p>Represents a point-of-sale transaction made by a seller. It holds the list of
 * {@link SaleItem} lines, the authoritative {@code total}, the chosen
 * {@link PaymentMethod}, an optional {@link PaymentReceipt} and the lifecycle
 * {@link SaleStatus}. The total is always recomputed from the line items so the
 * server remains the source of truth.</p>
 */
@Getter
public class Sale extends AbstractDomainAggregateRoot<Sale> {

    @Setter
    private Long id;
    private Long sellerId;
    private List<SaleItem> items;
    private double total;
    private PaymentMethod paymentMethod;
    private PaymentReceipt paymentReceipt;
    private SaleStatus status;
    private Instant createdAt;
    private Instant completedAt;

    public Sale() {
    }

    public Sale(Long sellerId, List<SaleItem> items, PaymentMethod paymentMethod,
                PaymentReceipt paymentReceipt, SaleStatus status) {
        this.sellerId = sellerId;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        this.total = computeTotal(this.items);
        this.paymentMethod = paymentMethod == null ? PaymentMethod.CASH : paymentMethod;
        this.paymentReceipt = paymentReceipt;
        this.status = status == null ? SaleStatus.IN_PROGRESS : status;
        this.createdAt = Instant.now();
        this.completedAt = this.status == SaleStatus.COMPLETED ? this.createdAt : null;
    }

    /**
     * Recomputes the sale total as the sum of every line subtotal.
     *
     * @param items the sale line items
     * @return the rounded total
     */
    private static double computeTotal(List<SaleItem> items) {
        double raw = items.stream().mapToDouble(SaleItem::subtotal).sum();
        return Math.round(raw * 100.0) / 100.0;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a sale that already carries identity and full state.
     */
    public void restoreState(Long id, Long sellerId, List<SaleItem> items, double total,
                             PaymentMethod paymentMethod, PaymentReceipt paymentReceipt,
                             SaleStatus status, Instant createdAt, Instant completedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.paymentReceipt = paymentReceipt;
        this.status = status;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }
}
