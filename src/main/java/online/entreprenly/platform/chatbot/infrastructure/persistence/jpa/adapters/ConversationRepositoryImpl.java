package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers.ConversationPersistenceAssembler;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories.ConversationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class ConversationRepositoryImpl implements ConversationRepository {

    private final ConversationPersistenceRepository persistenceRepository;

    public ConversationRepositoryImpl(ConversationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<Conversation> findAll() {
        return persistenceRepository.findAll().stream()
                .map(ConversationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Conversation> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(ConversationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Conversation> findAllBySellerId(Long sellerId) {
        return persistenceRepository.findAllBySellerIdOrderByIdDesc(sellerId).stream()
                .map(ConversationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Conversation> findByClientPhoneAndSellerId(String clientPhone, Long sellerId) {
        return persistenceRepository.findFirstByClientPhoneAndSellerIdOrderByIdDesc(clientPhone, sellerId)
                .map(ConversationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Conversation save(Conversation conversation) {
        var saved = persistenceRepository.save(
                ConversationPersistenceAssembler.toPersistenceFromDomain(conversation));
        return ConversationPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
