package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;

import java.util.List;

/**
 * Command to create an order captured through a conversation.
 *
 * @param conversationId  the conversation the order belongs to
 * @param orderNumber     the human-readable order number (nullable; generated when absent)
 * @param items           the ordered line items
 * @param deliveryAddress the delivery address agreed in the chat
 * @param paymentMethod   the payment method agreed in the chat
 * @param status          the initial status (nullable; defaults to pending)
 */
public record CreateChatOrderCommand(Long conversationId, String orderNumber, List<OrderItem> items,
                                     String deliveryAddress, String paymentMethod, OrderStatus status) {
}
