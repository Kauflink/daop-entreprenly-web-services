package online.entreprenly.platform.subscription.interfaces.rest.resources;

import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resource received to create a subscription plan.
 */
@Schema(name = "CreateSubscriptionPlanRequest", description = "Request to create a subscription plan")
public record CreateSubscriptionPlanResource(
        @Schema(description = "Plan stable code", example = "plan-control")
        String code,

        @Schema(description = "Plan name", example = "Premium")
        @NotBlank
        String name,

        @Schema(description = "Plan description", example = "Full access to advanced features")
        String description,

        @Schema(description = "Plan price amount", example = "49.90")
        @NotNull
        @DecimalMin("0.00")
        BigDecimal amount,

        @Schema(description = "Annual plan price amount", example = "890.00")
        @DecimalMin("0.00")
        BigDecimal annualAmount,

        @Schema(description = "Plan currency", example = "USD")
        @NotBlank
        String currency,

        @Schema(description = "Billing period", example = "MONTHLY")
        BillingPeriod billingPeriod,

        @Schema(description = "Feature keys included in this plan", example = "[\"dashboard\", \"advanced-reports\"]")
        List<String> features,

        @Schema(description = "Whether the plan is available for new subscriptions", example = "true")
        Boolean active
) {
}
