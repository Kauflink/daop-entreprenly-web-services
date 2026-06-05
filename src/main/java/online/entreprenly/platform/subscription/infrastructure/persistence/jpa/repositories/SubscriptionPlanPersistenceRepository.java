package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for subscription plan persistence entities.
 */
@Repository
public interface SubscriptionPlanPersistenceRepository extends JpaRepository<SubscriptionPlanPersistenceEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByCode(String code);
    Optional<SubscriptionPlanPersistenceEntity> findByCode(String code);
}
