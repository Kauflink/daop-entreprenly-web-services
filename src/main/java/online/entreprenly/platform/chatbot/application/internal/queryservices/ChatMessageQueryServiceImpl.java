package online.entreprenly.platform.chatbot.application.internal.queryservices;

import online.entreprenly.platform.chatbot.application.queryservices.ChatMessageQueryService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatMessagesQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatMessagesByConversationIdQuery;
import online.entreprenly.platform.chatbot.domain.repositories.ChatMessageRepository;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Chat message query service implementation.
 */
@Service
public class ChatMessageQueryServiceImpl implements ChatMessageQueryService {

    private final ChatMessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public ChatMessageQueryServiceImpl(ChatMessageRepository messageRepository,
                                       ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public List<ChatMessage> handle(GetAllChatMessagesQuery query) {
        var conversationIds = conversationRepository.findAllBySellerId(query.sellerId())
                .stream().map(c -> c.getId()).toList();
        return messageRepository.findByConversationIdIn(conversationIds);
    }

    @Override
    public List<ChatMessage> handle(GetChatMessagesByConversationIdQuery query) {
        return messageRepository.findByConversationId(query.conversationId());
    }
}
