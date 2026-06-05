package online.entreprenly.platform.chatbot.domain.model.queries;

/**
 * Query to retrieve a WhatsApp session by its identifier.
 *
 * @param sessionId the session identifier
 */
public record GetWhatsappSessionByIdQuery(Long sessionId) {
}
