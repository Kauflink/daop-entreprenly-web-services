package online.entreprenly.platform.subscription.application.queryservices;

import online.entreprenly.platform.subscription.domain.model.aggregates.Payment;
import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetPaymentsBySubscriptionIdQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetSubscriptionByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Subscription query service.
 */
public interface SubscriptionQueryService {
    Optional<Subscription> handle(GetSubscriptionByIdQuery query);
    Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query);
    List<Payment> handle(GetPaymentsBySubscriptionIdQuery query);
}
