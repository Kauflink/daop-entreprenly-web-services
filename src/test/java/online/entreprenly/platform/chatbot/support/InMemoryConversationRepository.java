package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory {@link ConversationRepository} for fast, DB-free application tests.
 */
public class InMemoryConversationRepository implements ConversationRepository {

    private final Map<Long, Conversation> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public List<Conversation> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Conversation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Conversation> findByClientPhone(String clientPhone) {
        return store.values().stream()
                .filter(c -> clientPhone.equals(c.getClientPhone()))
                .max(Comparator.comparing(Conversation::getId));
    }

    @Override
    public Conversation save(Conversation conversation) {
        if (conversation.getId() == null) {
            conversation.setId(sequence.incrementAndGet());
        }
        store.put(conversation.getId(), conversation);
        return conversation;
    }
}
