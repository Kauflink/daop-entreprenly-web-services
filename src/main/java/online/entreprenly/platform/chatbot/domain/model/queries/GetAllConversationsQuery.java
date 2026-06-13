package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve every conversation that belongs to a given seller.
 *
 * @param sellerId the seller whose conversations to retrieve
 */
public record GetAllConversationsQuery(Long sellerId) {
}
