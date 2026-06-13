package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.domain.repositories.WhatsappSessionRepository;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers.WhatsappSessionPersistenceAssembler;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories.WhatsappSessionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the WhatsApp session domain port with Spring Data JPA.
 */
@Repository
public class WhatsappSessionRepositoryImpl implements WhatsappSessionRepository {

    private final WhatsappSessionPersistenceRepository persistenceRepository;

    public WhatsappSessionRepositoryImpl(WhatsappSessionPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<WhatsappSession> findAll() {
        return persistenceRepository.findAll().stream()
                .map(WhatsappSessionPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<WhatsappSession> findBySellerId(Long sellerId) {
        return persistenceRepository.findBySellerIdOrderByIdDesc(sellerId).stream()
                .map(WhatsappSessionPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<WhatsappSession> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(WhatsappSessionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public WhatsappSession save(WhatsappSession session) {
        var saved = persistenceRepository.save(
                WhatsappSessionPersistenceAssembler.toPersistenceFromDomain(session));
        return WhatsappSessionPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
