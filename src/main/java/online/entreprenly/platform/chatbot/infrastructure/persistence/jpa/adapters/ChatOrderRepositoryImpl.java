package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.repositories.ChatOrderRepository;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers.ChatOrderPersistenceAssembler;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories.ChatOrderPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the chat order domain port with Spring Data JPA.
 */
@Repository
public class ChatOrderRepositoryImpl implements ChatOrderRepository {

    private final ChatOrderPersistenceRepository persistenceRepository;

    public ChatOrderRepositoryImpl(ChatOrderPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<ChatOrder> findAll() {
        return persistenceRepository.findAll().stream()
                .map(ChatOrderPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<ChatOrder> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(ChatOrderPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<ChatOrder> findByConversationId(Long conversationId) {
        return persistenceRepository.findByConversationIdOrderByIdDesc(conversationId).stream()
                .map(ChatOrderPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<ChatOrder> findByConversationIdIn(List<Long> conversationIds) {
        if (conversationIds == null || conversationIds.isEmpty()) {
            return List.of();
        }
        return persistenceRepository.findByConversationIdInOrderByIdDesc(conversationIds).stream()
                .map(ChatOrderPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public long count() {
        return persistenceRepository.count();
    }

    @Override
    public ChatOrder save(ChatOrder order) {
        var saved = persistenceRepository.save(
                ChatOrderPersistenceAssembler.toPersistenceFromDomain(order));
        return ChatOrderPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
