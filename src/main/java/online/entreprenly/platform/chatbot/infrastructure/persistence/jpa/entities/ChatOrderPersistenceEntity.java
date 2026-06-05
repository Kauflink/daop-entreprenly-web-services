package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.embeddables.OrderItemEmbeddable;
import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
 * JPA persistence entity for chat orders.
 */
@Entity
@Table(name = "chat_orders")
@Getter
@Setter
@NoArgsConstructor
public class ChatOrderPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "order_number", length = 30)
    private String orderNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chat_order_items", joinColumns = @JoinColumn(name = "chat_order_id"))
    private List<OrderItemEmbeddable> items = new ArrayList<>();

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "delivery_address", length = 300)
    private String deliveryAddress;

    @Column(name = "payment_method", length = 40)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private OrderStatus status;

    @Column(name = "has_receipt", nullable = false)
    private boolean hasReceipt;

    @Column(name = "rejection_count", nullable = false)
    private int rejectionCount;

    @Column(name = "order_created_at")
    private Instant orderCreatedAt;
}
