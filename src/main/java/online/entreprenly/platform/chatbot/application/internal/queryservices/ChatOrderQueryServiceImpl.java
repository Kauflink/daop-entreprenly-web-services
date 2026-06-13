package online.entreprenly.platform.chatbot.application.internal.queryservices;

import online.entreprenly.platform.chatbot.application.queryservices.ChatOrderQueryService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatOrdersQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatOrderByIdQuery;
import online.entreprenly.platform.chatbot.domain.repositories.ChatOrderRepository;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Chat order query service implementation.
 */
@Service
public class ChatOrderQueryServiceImpl implements ChatOrderQueryService {

    private final ChatOrderRepository orderRepository;
    private final ConversationRepository conversationRepository;

    public ChatOrderQueryServiceImpl(ChatOrderRepository orderRepository,
                                     ConversationRepository conversationRepository) {
        this.orderRepository = orderRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public List<ChatOrder> handle(GetAllChatOrdersQuery query) {
        var conversationIds = conversationRepository.findAllBySellerId(query.sellerId())
                .stream().map(c -> c.getId()).toList();
        return orderRepository.findByConversationIdIn(conversationIds);
    }

    @Override
    public Optional<ChatOrder> handle(GetChatOrderByIdQuery query) {
        return orderRepository.findById(query.orderId());
    }
}
