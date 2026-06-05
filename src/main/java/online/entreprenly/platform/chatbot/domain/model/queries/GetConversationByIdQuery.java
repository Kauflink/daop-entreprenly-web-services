package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve a conversation by its identifier.
 *
 * @param conversationId the conversation identifier
 */
public record GetConversationByIdQuery(Long conversationId) {
}
