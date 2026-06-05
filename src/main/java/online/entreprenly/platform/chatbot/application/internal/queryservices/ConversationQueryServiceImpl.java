package online.entreprenly.platform.chatbot.application.internal.queryservices;

import online.entreprenly.platform.chatbot.application.queryservices.ConversationQueryService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllConversationsQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetConversationByIdQuery;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Conversation query service implementation.
 */
@Service
public class ConversationQueryServiceImpl implements ConversationQueryService {

    private final ConversationRepository conversationRepository;

    public ConversationQueryServiceImpl(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public List<Conversation> handle(GetAllConversationsQuery query) {
        return conversationRepository.findAll();
    }

    @Override
    public Optional<Conversation> handle(GetConversationByIdQuery query) {
        return conversationRepository.findById(query.conversationId());
    }
}
