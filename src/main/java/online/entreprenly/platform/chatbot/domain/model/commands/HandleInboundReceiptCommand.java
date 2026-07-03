package online.entreprenly.platform.chatbot.domain.model.commands;


public record HandleInboundReceiptCommand(String fromPhone, String ownerEmail, String image) {
}
