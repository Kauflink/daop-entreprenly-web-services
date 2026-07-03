package online.entreprenly.platform.chatbot.domain.model.commands;


public record ConfirmChatOrderDeliveryCommand(Long orderId, String deliveryAddress) {
}
