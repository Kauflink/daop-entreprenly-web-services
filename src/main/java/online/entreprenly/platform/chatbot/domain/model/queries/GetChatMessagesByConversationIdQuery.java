package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve the messages of a single conversation, ordered chronologically.
 *
 * @param conversationId the conversation identifier
 */
public record GetChatMessagesByConversationIdQuery(Long conversationId) {
}
