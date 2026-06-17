package online.entreprenly.platform.chatbot.domain.model.aggregates;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
public class ChatOrder extends AbstractDomainAggregateRoot<ChatOrder> {

    
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

    
    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
        this.hasReceipt = true;
    }

    
    public void rejectReceipt() {
        this.rejectionCount += 1;
        this.hasReceipt = false;
        this.status = this.rejectionCount >= MAX_RECEIPT_REJECTIONS
                ? OrderStatus.BLOCKED
                : OrderStatus.WAITING_PAYMENT;
    }

    
    public void attachReceipt() {
        this.hasReceipt = true;
        this.status = OrderStatus.WAITING_PAYMENT;
    }

    
    public void attachReceipt(String image) {
        attachReceipt();
        this.receiptImage = image;
    }

    
    public void changeStatus(OrderStatus status) {
        if (status == null) return;
        this.status = status;
    }

    
    public void confirmDelivery(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.WAITING_PAYMENT;
    }

    
    public boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    
    public boolean isBlocked() {
        return this.status == OrderStatus.BLOCKED;
    }

    
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
