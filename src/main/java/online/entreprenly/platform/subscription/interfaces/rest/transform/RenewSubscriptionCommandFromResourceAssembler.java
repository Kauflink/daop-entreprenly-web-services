package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.commands.RenewSubscriptionCommand;
import online.entreprenly.platform.subscription.interfaces.rest.resources.RenewSubscriptionResource;

/**
 * Assembler from renew subscription resource to command.
 */
public final class RenewSubscriptionCommandFromResourceAssembler {

    private RenewSubscriptionCommandFromResourceAssembler() {
    }

    public static RenewSubscriptionCommand toCommandFromResource(Long subscriptionId, RenewSubscriptionResource resource) {
        return new RenewSubscriptionCommand(
                subscriptionId,
                resource.paymentMethod(),
                resource.cardToken(),
                resource.requestedPaymentStatus());
    }
}
