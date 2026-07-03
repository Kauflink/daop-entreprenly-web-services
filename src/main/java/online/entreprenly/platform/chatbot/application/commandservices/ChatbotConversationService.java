package online.entreprenly.platform.chatbot.application.commandservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundReceiptCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;


public interface ChatbotConversationService {

    
    Result<ChatMessage, ApplicationError> handle(HandleInboundMessageCommand command);

    
    Result<ChatMessage, ApplicationError> handle(HandleInboundReceiptCommand command);
}
