package online.entreprenly.platform.subscription.domain.model.commands;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;

/**
 * Command to create a subscription and process its first fake payment.
 */
public record CreateSubscriptionCommand(Long userId, Long planId, String paymentMethod, String cardToken,
                                        PaymentStatus requestedPaymentStatus) {
}
