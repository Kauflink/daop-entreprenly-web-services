package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a user subscription.
 */
@Schema(name = "SubscriptionResponse", description = "A user subscription")
public record SubscriptionResource(
        @Schema(description = "Subscription unique identifier", example = "1")
        Long id,

        @Schema(description = "IAM user identifier", example = "1")
        Long userId,

        @Schema(description = "Subscription plan identifier", example = "1")
        Long planId,

        @Schema(description = "Subscription status", example = "ACTIVE")
        SubscriptionStatus status,

        @Schema(description = "Instant the subscription was started", example = "2026-06-04T15:04:05Z")
        Instant startedAt,

        @Schema(description = "Current billing period end", example = "2026-07-04T15:04:05Z")
        Instant currentPeriodEnd,

        @Schema(description = "Instant the subscription was cancelled", nullable = true)
        Instant cancelledAt,

        @Schema(description = "Latest fake payment identifier", example = "1", nullable = true)
        Long latestPaymentId
) {
}
