package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;

/**
 * Command to advance a chat order through its payment-review lifecycle.
 *
 * <p>The desired {@code status} together with {@code hasReceipt} expresses the
 * seller's intent; the aggregate remains authoritative over the rejection count
 * and any resulting block.</p>
 *
 * @param orderId    the order identifier
 * @param status     the desired status
 * @param hasReceipt whether a payment receipt is currently attached
 */
public record UpdateChatOrderCommand(Long orderId, OrderStatus status, boolean hasReceipt) {
}
