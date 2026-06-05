package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Resource received to create a subscription and process its first fake payment.
 */
@Schema(name = "CreateSubscriptionRequest", description = "Request to create a subscription")
public record CreateSubscriptionResource(
        @Schema(description = "IAM user identifier", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Subscription plan identifier", example = "1")
        @NotNull
        Long planId,

        @Schema(description = "Fake payment method label", example = "FAKE_CARD")
        String paymentMethod,

        @Schema(description = "Fake card token; never a real card number", example = "fake-card-token")
        String cardToken,

        @Schema(description = "Requested fake payment result", example = "APPROVED")
        PaymentStatus requestedPaymentStatus,

        @Schema(description = "Selected billing period", example = "MONTHLY")
        BillingPeriod billingPeriod
) {
}
