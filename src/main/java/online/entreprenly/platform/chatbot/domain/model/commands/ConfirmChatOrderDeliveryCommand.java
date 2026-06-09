package online.entreprenly.platform.chatbot.domain.model.commands;

/**
 * Command to confirm a draft order's delivery address, moving it to awaiting payment.
 *
 * @param orderId         the order identifier
 * @param deliveryAddress the delivery address agreed in the chat
 */
public record ConfirmChatOrderDeliveryCommand(Long orderId, String deliveryAddress) {
}
