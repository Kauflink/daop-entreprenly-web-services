package online.entreprenly.platform.chatbot.application.queryservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllConversationsQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetConversationByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for conversation read operations.
 */
public interface ConversationQueryService {

    List<Conversation> handle(GetAllConversationsQuery query);

    Optional<Conversation> handle(GetConversationByIdQuery query);
}
