package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.repositories.ChatMessageRepository;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers.ChatMessagePersistenceAssembler;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories.ChatMessagePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final ChatMessagePersistenceRepository persistenceRepository;

    public ChatMessageRepositoryImpl(ChatMessagePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<ChatMessage> findAll() {
        return persistenceRepository.findAll().stream()
                .map(ChatMessagePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<ChatMessage> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(ChatMessagePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<ChatMessage> findByConversationId(Long conversationId) {
        return persistenceRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(ChatMessagePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<ChatMessage> findByConversationIdIn(List<Long> conversationIds) {
        if (conversationIds == null || conversationIds.isEmpty()) {
            return List.of();
        }
        return persistenceRepository.findByConversationIdInOrderBySentAtAsc(conversationIds).stream()
                .map(ChatMessagePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public ChatMessage save(ChatMessage message) {
        var saved = persistenceRepository.save(
                ChatMessagePersistenceAssembler.toPersistenceFromDomain(message));
        return ChatMessagePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
