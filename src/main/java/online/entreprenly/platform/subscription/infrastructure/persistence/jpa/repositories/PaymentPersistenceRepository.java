package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for payment persistence entities.
 */
@Repository
public interface PaymentPersistenceRepository extends JpaRepository<PaymentPersistenceEntity, Long> {
    List<PaymentPersistenceEntity> findBySubscriptionIdOrderByRequestedAtDesc(Long subscriptionId);
}
