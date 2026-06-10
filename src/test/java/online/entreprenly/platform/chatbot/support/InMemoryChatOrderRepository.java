package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.repositories.ChatOrderRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory {@link ChatOrderRepository} for fast, DB-free application tests.
 */
public class InMemoryChatOrderRepository implements ChatOrderRepository {

    private final Map<Long, ChatOrder> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public List<ChatOrder> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<ChatOrder> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ChatOrder> findByConversationId(Long conversationId) {
        return store.values().stream()
                .filter(o -> conversationId.equals(o.getConversationId()))
                .sorted(java.util.Comparator.comparing(ChatOrder::getId).reversed())
                .toList();
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public ChatOrder save(ChatOrder order) {
        if (order.getId() == null) {
            order.setId(sequence.incrementAndGet());
        }
        store.put(order.getId(), order);
        return order;
    }
}
