package online.entreprenly.platform.chatbot.domain.model.commands;


public record CreateConversationCommand(Long sellerId, String clientPhone, String clientName) {
}
