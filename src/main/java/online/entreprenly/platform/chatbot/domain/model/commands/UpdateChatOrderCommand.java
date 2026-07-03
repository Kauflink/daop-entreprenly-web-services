package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;


public record UpdateChatOrderCommand(Long orderId, OrderStatus status, boolean hasReceipt) {
}
