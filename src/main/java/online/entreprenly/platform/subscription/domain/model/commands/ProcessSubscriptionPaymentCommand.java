package online.entreprenly.platform.subscription.domain.model.commands;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;

/**
 * Command to process a fake payment for an existing subscription.
 */
public record ProcessSubscriptionPaymentCommand(Long subscriptionId, String paymentMethod, String cardToken,
                                                PaymentStatus requestedPaymentStatus, BillingPeriod billingPeriod) {
}
