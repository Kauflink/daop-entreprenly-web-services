package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve WhatsApp sessions owned by a specific seller.
 */
public record GetAllWhatsappSessionsQuery(Long sellerId) {
}
