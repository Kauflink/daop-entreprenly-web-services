package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.ChatOrderResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.OrderItemResource;

import java.util.List;

/**
 * Assembler that converts {@link ChatOrder} aggregates into resources.
 */
public final class ChatOrderResourceFromEntityAssembler {

    private ChatOrderResourceFromEntityAssembler() {
    }

    public static ChatOrderResource toResourceFromEntity(ChatOrder order) {
        List<OrderItemResource> items = order.getItems().stream()
                .map(item -> new OrderItemResource(item.productName(), item.quantity(), item.unitPrice()))
                .toList();
        return new ChatOrderResource(
                order.getId(),
                order.getConversationId(),
                order.getOrderNumber(),
                items,
                order.getTotal(),
                order.getDeliveryAddress(),
                order.getPaymentMethod(),
                order.getStatus(),
                order.isHasReceipt(),
                order.getRejectionCount(),
                order.getCreatedAt(),
                order.getReceiptImage());
    }
}
