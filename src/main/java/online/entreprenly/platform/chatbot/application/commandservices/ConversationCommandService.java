package online.entreprenly.platform.chatbot.application.commandservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateConversationCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateConversationCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;


public interface ConversationCommandService {

    Result<Conversation, ApplicationError> handle(CreateConversationCommand command);

    Result<Conversation, ApplicationError> handle(UpdateConversationCommand command);
}
