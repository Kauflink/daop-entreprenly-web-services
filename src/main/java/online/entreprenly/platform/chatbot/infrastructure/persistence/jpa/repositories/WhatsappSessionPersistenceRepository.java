package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.WhatsappSessionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for WhatsApp session persistence entities.
 */
@Repository
public interface WhatsappSessionPersistenceRepository extends JpaRepository<WhatsappSessionPersistenceEntity, Long> {

    java.util.List<WhatsappSessionPersistenceEntity> findBySellerIdOrderByIdDesc(Long sellerId);
}
