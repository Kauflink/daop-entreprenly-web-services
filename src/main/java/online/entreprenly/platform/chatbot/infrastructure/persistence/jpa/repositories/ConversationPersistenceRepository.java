package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.ConversationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for conversation persistence entities.
 */
@Repository
public interface ConversationPersistenceRepository extends JpaRepository<ConversationPersistenceEntity, Long> {

    Optional<ConversationPersistenceEntity> findFirstByClientPhoneOrderByIdDesc(String clientPhone);
}
