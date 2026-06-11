package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve every chat message that belongs to a given seller's conversations.
 *
 * @param sellerId the seller whose messages to retrieve
 */
public record GetAllChatMessagesQuery(Long sellerId) {
}
