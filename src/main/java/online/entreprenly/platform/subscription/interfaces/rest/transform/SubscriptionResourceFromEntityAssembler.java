package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.interfaces.rest.resources.SubscriptionResource;

/**
 * Assembler from subscription aggregate to resource.
 */
public final class SubscriptionResourceFromEntityAssembler {

    private SubscriptionResourceFromEntityAssembler() {
    }

    public static SubscriptionResource toResourceFromEntity(Subscription subscription) {
        return new SubscriptionResource(
                subscription.getId(),
                subscription.getUserId(),
                subscription.getPlanId(),
                subscription.getStatus(),
                subscription.getStartedAt(),
                subscription.getCurrentPeriodEnd(),
                subscription.getCancelledAt(),
                subscription.getLatestPaymentId(),
                subscription.getBillingPeriod());
    }
}
