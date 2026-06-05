package online.entreprenly.platform.subscription.domain.model.queries;

/**
 * Query to get the active subscription for a user.
 */
public record GetActiveSubscriptionByUserIdQuery(Long userId) {
}
