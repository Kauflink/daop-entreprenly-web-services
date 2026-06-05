package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.domain.repositories.WhatsappSessionRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory {@link WhatsappSessionRepository} for fast, DB-free application tests.
 */
public class InMemoryWhatsappSessionRepository implements WhatsappSessionRepository {

    private final Map<Long, WhatsappSession> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public List<WhatsappSession> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<WhatsappSession> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public WhatsappSession save(WhatsappSession session) {
        if (session.getId() == null) {
            session.setId(sequence.incrementAndGet());
        }
        store.put(session.getId(), session);
        return session;
    }
}
