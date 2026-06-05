package online.entreprenly.platform.subscription.domain.model.queries;

/**
 * Query to list payment attempts for a subscription.
 */
public record GetPaymentsBySubscriptionIdQuery(Long subscriptionId) {
}
