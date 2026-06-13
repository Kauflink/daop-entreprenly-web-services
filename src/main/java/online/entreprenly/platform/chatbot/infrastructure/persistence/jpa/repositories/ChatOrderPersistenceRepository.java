package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.ChatOrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for chat order persistence entities.
 */
@Repository
public interface ChatOrderPersistenceRepository extends JpaRepository<ChatOrderPersistenceEntity, Long> {

    java.util.List<ChatOrderPersistenceEntity> findByConversationIdOrderByIdDesc(Long conversationId);

    java.util.List<ChatOrderPersistenceEntity> findByConversationIdInOrderByIdDesc(java.util.List<Long> conversationIds);
}
