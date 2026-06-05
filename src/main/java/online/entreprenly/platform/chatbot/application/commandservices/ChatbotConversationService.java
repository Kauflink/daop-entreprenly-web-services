package online.entreprenly.platform.chatbot.application.commandservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service that orchestrates an inbound WhatsApp message: it routes the
 * message to a conversation, persists it, produces and persists the automatic reply,
 * sends that reply through the WhatsApp channel and pushes every change in real time.
 */
public interface ChatbotConversationService {

    /**
     * Handles an inbound client message end to end.
     *
     * @param command the inbound message
     * @return the persisted bot reply message, or an application error
     */
    Result<ChatMessage, ApplicationError> handle(HandleInboundMessageCommand command);
}
