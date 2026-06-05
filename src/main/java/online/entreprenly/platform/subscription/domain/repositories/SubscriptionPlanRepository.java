package online.entreprenly.platform.subscription.domain.repositories;

import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

/**
 * Subscription plan repository port.
 */
public interface SubscriptionPlanRepository {
    List<SubscriptionPlan> findAll();
    Optional<SubscriptionPlan> findById(Long id);
    Optional<SubscriptionPlan> findByCode(String code);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByCode(String code);
    SubscriptionPlan save(SubscriptionPlan plan);
}
