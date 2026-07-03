package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.ChatMessagePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatMessagePersistenceRepository extends JpaRepository<ChatMessagePersistenceEntity, Long> {

    List<ChatMessagePersistenceEntity> findByConversationIdOrderBySentAtAsc(Long conversationId);

    List<ChatMessagePersistenceEntity> findByConversationIdInOrderBySentAtAsc(List<Long> conversationIds);
}
