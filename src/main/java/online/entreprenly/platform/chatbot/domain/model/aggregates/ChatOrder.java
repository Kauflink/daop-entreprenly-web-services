package online.entreprenly.platform.chatbot.domain.model.aggregates;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatOrder aggregate root.
 *
 * <p>Represents an order captured through a {@link Conversation}. It owns its
 * {@link OrderItem} lines, the authoritative {@code total} (always recomputed from
 * the lines), the {@link OrderStatus} lifecycle and the payment-receipt review flow
 * ({@code hasReceipt}, {@code rejectionCount}). After two receipt rejections the order
 * blocks itself to mirror the conversation's anti-fraud rule.</p>
 */
@Getter
public class ChatOrder extends AbstractDomainAggregateRoot<ChatOrder> {

    /** Number of receipt rejections after which an order is automatically blocked. */
    private static final int MAX_RECEIPT_REJECTIONS = 2;

    @Setter
    private Long id;
    private Long conversationId;
    private String orderNumber;
    private List<OrderItem> items;
    private double total;
    private String deliveryAddress;
    private String paymentMethod;
    private OrderStatus status;
    private boolean hasReceipt;
    private int rejectionCount;
    private Instant createdAt;
    private String receiptImage;

    public ChatOrder() {
    }

    public ChatOrder(Long conversationId, String orderNumber, List<OrderItem> items,
                     String deliveryAddress, String paymentMethod, OrderStatus status) {
        this.conversationId = conversationId;
        this.orderNumber = orderNumber;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        this.total = computeTotal(this.items);
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.status = status == null ? OrderStatus.PENDING : status;
        this.hasReceipt = false;
        this.rejectionCount = 0;
        this.createdAt = Instant.now();
    }

    private static double computeTotal(List<OrderItem> items) {
        double raw = items.stream().mapToDouble(OrderItem::subtotal).sum();
        return Math.round(raw * 100.0) / 100.0;
    }

    /**
     * Confirms the order once its payment has been approved.
     */
    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
        this.hasReceipt = true;
    }

    /**
     * Records a payment-receipt rejection. The order returns to
     * {@link OrderStatus#WAITING_PAYMENT} until it reaches the rejection limit,
     * after which it is blocked.
     */
    public void rejectReceipt() {
        this.rejectionCount += 1;
        this.hasReceipt = false;
        this.status = this.rejectionCount >= MAX_RECEIPT_REJECTIONS
                ? OrderStatus.BLOCKED
                : OrderStatus.WAITING_PAYMENT;
    }

    /**
     * Marks that the client has attached a payment receipt awaiting review.
     */
    public void attachReceipt() {
        this.hasReceipt = true;
        this.status = OrderStatus.WAITING_PAYMENT;
    }

    /**
     * Attaches a payment receipt together with its image (data URL) sent by the client.
     *
     * @param image the receipt image as a data URL (nullable)
     */
    public void attachReceipt(String image) {
        attachReceipt();
        this.receiptImage = image;
    }

    /**
     * Applies a status transition that is not part of the receipt-review flow
     * (e.g. cancelling or re-opening an order).
     *
     * @param status the desired status (ignored when null)
     */
    public void changeStatus(OrderStatus status) {
        if (status == null) return;
        this.status = status;
    }

    /**
     * Confirms the delivery address, moving the order from draft to awaiting payment.
     *
     * @param deliveryAddress the address agreed with the client
     */
    public void confirmDelivery(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.WAITING_PAYMENT;
    }

    /**
     * @return {@code true} while the order is still a draft awaiting its delivery address
     */
    public boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    /**
     * @return {@code true} when the order has reached its rejection limit
     */
    public boolean isBlocked() {
        return this.status == OrderStatus.BLOCKED;
    }

    /**
     * Restores an aggregate from persistence.
     */
    public void restoreState(Long id, Long conversationId, String orderNumber, List<OrderItem> items,
                             double total, String deliveryAddress, String paymentMethod, OrderStatus status,
                             boolean hasReceipt, int rejectionCount, Instant createdAt, String receiptImage) {
        this.id = id;
        this.conversationId = conversationId;
        this.orderNumber = orderNumber;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        this.total = total;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.hasReceipt = hasReceipt;
        this.rejectionCount = rejectionCount;
        this.createdAt = createdAt;
        this.receiptImage = receiptImage;
    }
}
