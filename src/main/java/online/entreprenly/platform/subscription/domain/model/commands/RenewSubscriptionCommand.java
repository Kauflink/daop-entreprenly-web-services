package online.entreprenly.platform.subscription.domain.model.commands;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;

/**
 * Command to renew a subscription through the fake payment gateway.
 */
public record RenewSubscriptionCommand(Long subscriptionId, String paymentMethod, String cardToken,
                                       PaymentStatus requestedPaymentStatus, BillingPeriod billingPeriod) {
}
