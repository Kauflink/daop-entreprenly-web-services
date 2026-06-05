package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve a chat order by its identifier.
 *
 * @param orderId the order identifier
 */
public record GetChatOrderByIdQuery(Long orderId) {
}
