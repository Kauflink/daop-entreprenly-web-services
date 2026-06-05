package online.entreprenly.platform.subscription.domain.repositories;

import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;

import java.util.Optional;

/**
 * Subscription repository port.
 */
public interface SubscriptionRepository {
    Optional<Subscription> findById(Long id);
    Optional<Subscription> findFirstByUserIdAndStatus(Long userId, SubscriptionStatus status);
    Subscription save(Subscription subscription);
}
