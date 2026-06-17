package online.entreprenly.platform.chatbot.application.queryservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatMessagesQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatMessagesByConversationIdQuery;

import java.util.List;


public interface ChatMessageQueryService {

    List<ChatMessage> handle(GetAllChatMessagesQuery query);

    List<ChatMessage> handle(GetChatMessagesByConversationIdQuery query);
}
