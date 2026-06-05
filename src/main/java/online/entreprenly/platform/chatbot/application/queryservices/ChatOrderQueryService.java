package online.entreprenly.platform.chatbot.application.queryservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatOrdersQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatOrderByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for chat order read operations.
 */
public interface ChatOrderQueryService {

    List<ChatOrder> handle(GetAllChatOrdersQuery query);

    Optional<ChatOrder> handle(GetChatOrderByIdQuery query);
}
