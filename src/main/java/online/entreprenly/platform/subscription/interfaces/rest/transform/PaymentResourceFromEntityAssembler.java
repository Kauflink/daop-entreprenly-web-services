package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.aggregates.Payment;
import online.entreprenly.platform.subscription.interfaces.rest.resources.PaymentResource;

/**
 * Assembler from payment aggregate to resource.
 */
public final class PaymentResourceFromEntityAssembler {

    private PaymentResourceFromEntityAssembler() {
    }

    public static PaymentResource toResourceFromEntity(Payment payment) {
        return new PaymentResource(
                payment.getId(),
                payment.getSubscriptionId(),
                payment.getUserId(),
                payment.getPlanId(),
                payment.getAmount().amount(),
                payment.getAmount().currency(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getProviderMessage(),
                payment.getRequestedStatus(),
                payment.getRequestedAt(),
                payment.getProcessedAt());
    }
}
