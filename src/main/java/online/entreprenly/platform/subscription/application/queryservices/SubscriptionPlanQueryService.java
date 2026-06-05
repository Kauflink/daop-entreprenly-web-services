package online.entreprenly.platform.subscription.application.queryservices;

import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.queries.GetAllSubscriptionPlansQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetSubscriptionPlanByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Subscription plan query service.
 */
public interface SubscriptionPlanQueryService {
    List<SubscriptionPlan> handle(GetAllSubscriptionPlansQuery query);
    Optional<SubscriptionPlan> handle(GetSubscriptionPlanByIdQuery query);
}
