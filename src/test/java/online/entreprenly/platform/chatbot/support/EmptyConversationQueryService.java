package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.application.queryservices.ConversationQueryService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllConversationsQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetConversationByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Test double for {@link ConversationQueryService} that knows no conversations.
 *
 * <p>Used where a collaborator only needs the dependency present (e.g. order
 * confirmation that resolves the seller from a conversation): returning empty
 * keeps the side effect a no-op without standing up the full read model.</p>
 */
public class EmptyConversationQueryService implements ConversationQueryService {

    @Override
    public List<Conversation> handle(GetAllConversationsQuery query) {
        return List.of();
    }

    @Override
    public Optional<Conversation> handle(GetConversationByIdQuery query) {
        return Optional.empty();
    }
}
