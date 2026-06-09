package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.embeddables.OrderItemEmbeddable;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.ChatOrderPersistenceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Static assembler between chat order domain and persistence representations.
 */
public final class ChatOrderPersistenceAssembler {

    private ChatOrderPersistenceAssembler() {
    }

    public static ChatOrder toDomainFromPersistence(ChatOrderPersistenceEntity entity) {
        if (entity == null) return null;
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> new OrderItem(item.getProductName(), item.getQuantity(), item.getUnitPrice()))
                .toList();
        var order = new ChatOrder();
        order.restoreState(
                entity.getId(),
                entity.getConversationId(),
                entity.getOrderNumber(),
                items,
                entity.getTotal(),
                entity.getDeliveryAddress(),
                entity.getPaymentMethod(),
                entity.getStatus(),
                entity.isHasReceipt(),
                entity.getRejectionCount(),
                entity.getOrderCreatedAt(),
                entity.getReceiptImage());
        return order;
    }

    public static ChatOrderPersistenceEntity toPersistenceFromDomain(ChatOrder order) {
        if (order == null) return null;
        var entity = new ChatOrderPersistenceEntity();
        if (order.getId() != null) {
            entity.setId(order.getId());
        }
        entity.setConversationId(order.getConversationId());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setItems(order.getItems().stream()
                .map(item -> new OrderItemEmbeddable(item.productName(), item.quantity(), item.unitPrice()))
                .collect(Collectors.toCollection(ArrayList::new)));
        entity.setTotal(order.getTotal());
        entity.setDeliveryAddress(order.getDeliveryAddress());
        entity.setPaymentMethod(order.getPaymentMethod());
        entity.setStatus(order.getStatus());
        entity.setHasReceipt(order.isHasReceipt());
        entity.setRejectionCount(order.getRejectionCount());
        entity.setOrderCreatedAt(order.getCreatedAt());
        entity.setReceiptImage(order.getReceiptImage());
        return entity;
    }
}
