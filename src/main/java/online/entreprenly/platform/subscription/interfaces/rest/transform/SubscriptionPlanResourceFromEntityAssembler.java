package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.interfaces.rest.resources.SubscriptionPlanResource;

/**
 * Assembler from subscription plan aggregate to resource.
 */
public final class SubscriptionPlanResourceFromEntityAssembler {

    private SubscriptionPlanResourceFromEntityAssembler() {
    }

    public static SubscriptionPlanResource toResourceFromEntity(SubscriptionPlan plan) {
        return new SubscriptionPlanResource(
                plan.getId(),
                plan.getCode(),
                plan.getName(),
                plan.getDescription(),
                plan.getPrice().amount(),
                plan.getAnnualPrice().amount(),
                plan.getPrice().currency(),
                plan.getBillingPeriod(),
                plan.getFeatures(),
                plan.isActive());
    }
}
