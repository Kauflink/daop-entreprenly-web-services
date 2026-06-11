package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.repositories.ChatMessageRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory {@link ChatMessageRepository} for fast, DB-free application tests.
 */
public class InMemoryChatMessageRepository implements ChatMessageRepository {

    private final Map<Long, ChatMessage> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public List<ChatMessage> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<ChatMessage> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ChatMessage> findByConversationId(Long conversationId) {
        return store.values().stream()
                .filter(m -> conversationId.equals(m.getConversationId()))
                .sorted(Comparator.comparing(ChatMessage::getSentAt))
                .toList();
    }

    @Override
    public List<ChatMessage> findByConversationIdIn(List<Long> conversationIds) {
        if (conversationIds.isEmpty()) return List.of();
        return store.values().stream()
                .filter(m -> conversationIds.contains(m.getConversationId()))
                .sorted(Comparator.comparing(ChatMessage::getSentAt))
                .toList();
    }

    @Override
    public ChatMessage save(ChatMessage message) {
        if (message.getId() == null) {
            message.setId(sequence.incrementAndGet());
        }
        store.put(message.getId(), message);
        return message;
    }
}
