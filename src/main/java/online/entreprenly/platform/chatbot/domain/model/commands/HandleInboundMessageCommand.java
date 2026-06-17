package online.entreprenly.platform.chatbot.domain.model.commands;


public record HandleInboundMessageCommand(String fromPhone, String clientName, String content, String ownerEmail) {
}
