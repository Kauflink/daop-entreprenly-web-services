package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resource representing a subscription plan.
 */
@Schema(name = "SubscriptionPlanResponse", description = "A subscription plan")
public record SubscriptionPlanResource(
        @Schema(description = "Plan unique identifier", example = "1")
        Long id,

        @Schema(description = "Plan stable code", example = "plan-control")
        String code,

        @Schema(description = "Plan name", example = "Premium")
        String name,

        @Schema(description = "Plan description", example = "Full access to advanced features")
        String description,

        @Schema(description = "Plan price amount", example = "49.90")
        BigDecimal amount,

        @Schema(description = "Annual plan price amount", example = "890.00")
        BigDecimal annualAmount,

        @Schema(description = "Plan currency", example = "USD")
        String currency,

        @Schema(description = "Billing period", example = "MONTHLY")
        BillingPeriod billingPeriod,

        @Schema(description = "Feature keys included in this plan")
        List<String> features,

        @Schema(description = "Whether the plan is available for new subscriptions", example = "true")
        boolean active
) {
}
