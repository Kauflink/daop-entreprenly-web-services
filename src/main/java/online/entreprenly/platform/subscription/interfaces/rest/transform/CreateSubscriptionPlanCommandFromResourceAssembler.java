package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.commands.CreateSubscriptionPlanCommand;
import online.entreprenly.platform.subscription.interfaces.rest.resources.CreateSubscriptionPlanResource;

/**
 * Assembler from create subscription plan resource to command.
 */
public final class CreateSubscriptionPlanCommandFromResourceAssembler {

    private CreateSubscriptionPlanCommandFromResourceAssembler() {
    }

    public static CreateSubscriptionPlanCommand toCommandFromResource(CreateSubscriptionPlanResource resource) {
        return new CreateSubscriptionPlanCommand(
                resource.name(),
                resource.description(),
                resource.amount(),
                resource.currency(),
                resource.billingPeriod(),
                resource.features(),
                resource.active());
    }
}
