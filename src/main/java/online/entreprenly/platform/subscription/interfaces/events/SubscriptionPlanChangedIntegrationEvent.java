package online.entreprenly.platform.subscription.interfaces.events;

/**
 * Integration event published by the Subscription bounded context when a user's active plan
 * changes (e.g. a simulated purchase of Plan Control, or a downgrade to Plan Free).
 *
 * <p>This is the <em>published language</em> of the Subscription context. Other bounded contexts
 * (e.g. {@code profile}) must listen to this event rather than reaching into the subscription
 * domain directly.</p>
 *
 * @param userId   the identifier of the user whose plan changed
 * @param planName the human-readable name of the new active plan (e.g. {@code Plan Control})
 */
public record SubscriptionPlanChangedIntegrationEvent(Long userId, String planName) {
}
