package online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleStatus;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.embeddables.PaymentReceiptEmbeddable;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.embeddables.SaleItemEmbeddable;
import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA persistence entity for sales.
 */
@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
public class SalePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sale_items", joinColumns = @JoinColumn(name = "sale_id"))
    private List<SaleItemEmbeddable> items = new ArrayList<>();

    @Column(name = "total", nullable = false)
    private double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;

    @Embedded
    private PaymentReceiptEmbeddable paymentReceipt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private SaleStatus status;

    @Column(name = "sale_created_at")
    private Instant saleCreatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;
}
