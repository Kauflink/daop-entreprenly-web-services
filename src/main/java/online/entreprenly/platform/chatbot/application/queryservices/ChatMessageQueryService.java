package online.entreprenly.platform.chatbot.application.queryservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatMessagesQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatMessagesByConversationIdQuery;

import java.util.List;

/**
 * Query service for chat message read operations.
 */
public interface ChatMessageQueryService {

    List<ChatMessage> handle(GetAllChatMessagesQuery query);

    List<ChatMessage> handle(GetChatMessagesByConversationIdQuery query);
}
