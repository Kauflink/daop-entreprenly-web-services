package online.entreprenly.platform.chatbot.domain.model.commands;


public record AttachReceiptCommand(Long orderId, String receiptImage) {
}
