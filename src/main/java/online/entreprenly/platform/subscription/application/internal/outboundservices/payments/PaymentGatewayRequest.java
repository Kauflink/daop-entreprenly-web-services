package online.entreprenly.platform.subscription.application.internal.outboundservices.payments;

import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;

/**
 * Fake payment gateway request.
 */
public record PaymentGatewayRequest(Long subscriptionId, Long userId, Long planId, Money amount,
                                    String paymentMethod, String cardToken, PaymentStatus requestedStatus) {
}
