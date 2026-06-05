package online.entreprenly.platform.subscription.domain.model.commands;

/**
 * Command to cancel a subscription.
 */
public record CancelSubscriptionCommand(Long subscriptionId) {
}
