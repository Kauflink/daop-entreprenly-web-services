package online.entreprenly.platform.chatbot.application.commandservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.commands.ConfirmChatOrderDeliveryCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateChatOrderCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Command service for chat order write operations.
 */
public interface ChatOrderCommandService {

    Result<ChatOrder, ApplicationError> handle(CreateChatOrderCommand command);

    Result<ChatOrder, ApplicationError> handle(UpdateChatOrderCommand command);

    Result<ChatOrder, ApplicationError> handle(ConfirmChatOrderDeliveryCommand command);
}
