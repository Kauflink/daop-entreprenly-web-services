package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource received to process a fake payment attempt.
 */
@Schema(name = "ProcessSubscriptionPaymentRequest", description = "Request to process a fake payment")
public record ProcessSubscriptionPaymentResource(
        @Schema(description = "Fake payment method label", example = "FAKE_CARD")
        String paymentMethod,

        @Schema(description = "Fake card token; never a real card number", example = "fake-card-token")
        String cardToken,

        @Schema(description = "Requested fake payment result", example = "DECLINED")
        PaymentStatus requestedPaymentStatus,

        @Schema(description = "Selected billing period", example = "MONTHLY")
        BillingPeriod billingPeriod
) {
}
