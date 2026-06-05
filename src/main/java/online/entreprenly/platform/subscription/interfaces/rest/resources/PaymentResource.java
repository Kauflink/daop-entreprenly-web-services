package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resource representing a fake subscription payment.
 */
@Schema(name = "SubscriptionPaymentResponse", description = "A fake subscription payment attempt")
public record PaymentResource(
        @Schema(description = "Payment unique identifier", example = "1")
        Long id,

        @Schema(description = "Subscription identifier", example = "1")
        Long subscriptionId,

        @Schema(description = "IAM user identifier", example = "1")
        Long userId,

        @Schema(description = "Plan identifier", example = "1")
        Long planId,

        @Schema(description = "Payment amount", example = "49.90")
        BigDecimal amount,

        @Schema(description = "Payment currency", example = "USD")
        String currency,

        @Schema(description = "Fake payment method label", example = "FAKE_CARD")
        String paymentMethod,

        @Schema(description = "Fake payment status", example = "APPROVED")
        PaymentStatus status,

        @Schema(description = "Fake transaction identifier", example = "fake_tx_123")
        String transactionId,

        @Schema(description = "Fake provider message", example = "Fake payment approved")
        String providerMessage,

        @Schema(description = "Requested fake payment status", example = "APPROVED")
        PaymentStatus requestedStatus,

        @Schema(description = "Instant the fake payment was requested")
        Instant requestedAt,

        @Schema(description = "Instant the fake payment was processed", nullable = true)
        Instant processedAt
) {
}
