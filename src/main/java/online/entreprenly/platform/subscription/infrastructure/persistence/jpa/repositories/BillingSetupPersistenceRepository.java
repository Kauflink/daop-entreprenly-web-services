package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.BillingSetupPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for billing setup persistence entities.
 */
@Repository
public interface BillingSetupPersistenceRepository extends JpaRepository<BillingSetupPersistenceEntity, Long> {
    Optional<BillingSetupPersistenceEntity> findByUserId(Long userId);
}
