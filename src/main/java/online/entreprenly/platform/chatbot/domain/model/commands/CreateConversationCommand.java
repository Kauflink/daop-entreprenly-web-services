package online.entreprenly.platform.chatbot.domain.model.commands;

/**
 * Command to start a new conversation with a client.
 *
 * @param sellerId    the seller that owns the conversation
 * @param clientPhone the client's WhatsApp phone number
 * @param clientName  the client's display name
 */
public record CreateConversationCommand(Long sellerId, String clientPhone, String clientName) {
}
