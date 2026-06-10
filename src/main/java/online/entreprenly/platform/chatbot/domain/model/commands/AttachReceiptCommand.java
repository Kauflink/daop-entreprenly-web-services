package online.entreprenly.platform.chatbot.domain.model.commands;

/**
 * Command to attach a payment receipt (and its image) to an order awaiting payment.
 *
 * @param orderId      the order identifier
 * @param receiptImage the receipt image as a data URL (nullable)
 */
public record AttachReceiptCommand(Long orderId, String receiptImage) {
}
