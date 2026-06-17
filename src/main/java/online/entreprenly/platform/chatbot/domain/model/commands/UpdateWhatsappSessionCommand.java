package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.SessionStatus;


public record UpdateWhatsappSessionCommand(Long sessionId, SessionStatus status) {
}
