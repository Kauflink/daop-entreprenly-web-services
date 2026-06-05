package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing subscription access validation.
 */
@Schema(name = "SubscriptionAccessValidationResponse", description = "Subscription access validation result")
public record AccessValidationResource(
        @Schema(description = "IAM user identifier", example = "1")
        Long userId,

        @Schema(description = "Feature key checked", example = "advanced-reports", nullable = true)
        String feature,

        @Schema(description = "Whether access is allowed", example = "true")
        boolean hasAccess,

        @Schema(description = "Active subscription identifier", example = "1", nullable = true)
        Long subscriptionId,

        @Schema(description = "Plan identifier", example = "1", nullable = true)
        Long planId,

        @Schema(description = "Subscription status", example = "ACTIVE", nullable = true)
        SubscriptionStatus status
) {
}
