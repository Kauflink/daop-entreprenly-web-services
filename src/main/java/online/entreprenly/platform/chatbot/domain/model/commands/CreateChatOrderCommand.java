package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;

import java.util.List;


public record CreateChatOrderCommand(Long conversationId, String orderNumber, List<OrderItem> items,
                                     String deliveryAddress, String paymentMethod, OrderStatus status) {
}
