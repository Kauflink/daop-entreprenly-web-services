package online.entreprenly.platform.subscription.application.internal.outboundservices.payments;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;

/**
 * Fake payment gateway response.
 */
public record PaymentGatewayResponse(String transactionId, PaymentStatus status, String message) {
}
