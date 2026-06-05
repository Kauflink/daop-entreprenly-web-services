package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.commands.ProcessSubscriptionPaymentCommand;
import online.entreprenly.platform.subscription.interfaces.rest.resources.ProcessSubscriptionPaymentResource;

/**
 * Assembler from process subscription payment resource to command.
 */
public final class ProcessSubscriptionPaymentCommandFromResourceAssembler {

    private ProcessSubscriptionPaymentCommandFromResourceAssembler() {
    }

    public static ProcessSubscriptionPaymentCommand toCommandFromResource(Long subscriptionId,
                                                                          ProcessSubscriptionPaymentResource resource) {
        return new ProcessSubscriptionPaymentCommand(
                subscriptionId,
                resource.paymentMethod(),
                resource.cardToken(),
                resource.requestedPaymentStatus(),
                resource.billingPeriod());
    }
}
