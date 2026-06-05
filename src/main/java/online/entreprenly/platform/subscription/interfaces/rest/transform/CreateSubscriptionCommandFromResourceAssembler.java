package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.commands.CreateSubscriptionCommand;
import online.entreprenly.platform.subscription.interfaces.rest.resources.CreateSubscriptionResource;

/**
 * Assembler from create subscription resource to command.
 */
public final class CreateSubscriptionCommandFromResourceAssembler {

    private CreateSubscriptionCommandFromResourceAssembler() {
    }

    public static CreateSubscriptionCommand toCommandFromResource(CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(
                resource.userId(),
                resource.planId(),
                resource.paymentMethod(),
                resource.cardToken(),
                resource.requestedPaymentStatus(),
                resource.billingPeriod());
    }
}
