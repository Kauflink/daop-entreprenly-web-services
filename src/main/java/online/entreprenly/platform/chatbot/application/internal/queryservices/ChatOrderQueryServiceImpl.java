package online.entreprenly.platform.chatbot.application.internal.queryservices;

import online.entreprenly.platform.chatbot.application.queryservices.ChatOrderQueryService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatOrdersQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatOrderByIdQuery;
import online.entreprenly.platform.chatbot.domain.repositories.ChatOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Chat order query service implementation.
 */
@Service
public class ChatOrderQueryServiceImpl implements ChatOrderQueryService {

    private final ChatOrderRepository orderRepository;

    public ChatOrderQueryServiceImpl(ChatOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<ChatOrder> handle(GetAllChatOrdersQuery query) {
        return orderRepository.findAll();
    }

    @Override
    public Optional<ChatOrder> handle(GetChatOrderByIdQuery query) {
        return orderRepository.findById(query.orderId());
    }
}
