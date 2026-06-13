package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve every chat order that belongs to a given seller.
 *
 * @param sellerId the seller whose orders to retrieve
 */
public record GetAllChatOrdersQuery(Long sellerId) {
}
