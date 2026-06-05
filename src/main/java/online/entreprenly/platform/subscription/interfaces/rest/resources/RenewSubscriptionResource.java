package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource received to renew a subscription through the fake payment gateway.
 */
@Schema(name = "RenewSubscriptionRequest", description = "Request to renew a subscription")
public record RenewSubscriptionResource(
        @Schema(description = "Fake payment method label", example = "FAKE_CARD")
        String paymentMethod,

        @Schema(description = "Fake card token; never a real card number", example = "fake-card-token")
        String cardToken,

        @Schema(description = "Requested fake payment result", example = "APPROVED")
        PaymentStatus requestedPaymentStatus
) {
}
