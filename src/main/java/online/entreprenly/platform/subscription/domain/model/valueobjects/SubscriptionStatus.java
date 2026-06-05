package online.entreprenly.platform.subscription.domain.model.valueobjects;

/**
 * Subscription lifecycle status.
 */
public enum SubscriptionStatus {
    ACTIVE,
    PENDING_PAYMENT,
    CANCELLED,
    EXPIRED,
    SUSPENDED
}
