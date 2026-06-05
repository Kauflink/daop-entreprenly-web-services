package online.entreprenly.platform.subscription.application.internal.queryservices;

import online.entreprenly.platform.subscription.application.queryservices.SubscriptionPlanQueryService;
import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.queries.GetAllSubscriptionPlansQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetSubscriptionPlanByIdQuery;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Subscription plan query service implementation.
 */
@Service
public class SubscriptionPlanQueryServiceImpl implements SubscriptionPlanQueryService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanQueryServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public List<SubscriptionPlan> handle(GetAllSubscriptionPlansQuery query) {
        return subscriptionPlanRepository.findAll();
    }

    @Override
    public Optional<SubscriptionPlan> handle(GetSubscriptionPlanByIdQuery query) {
        return subscriptionPlanRepository.findById(query.planId());
    }
}
