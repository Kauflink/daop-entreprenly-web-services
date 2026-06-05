package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.SessionStatus;

/**
 * Command to update the connection status of an existing WhatsApp session.
 *
 * @param sessionId the session identifier
 * @param status    the desired session status
 */
public record UpdateWhatsappSessionCommand(Long sessionId, SessionStatus status) {
}
