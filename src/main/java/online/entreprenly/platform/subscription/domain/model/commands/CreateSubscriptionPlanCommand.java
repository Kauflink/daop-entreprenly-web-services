package online.entreprenly.platform.subscription.domain.model.commands;

import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;

import java.math.BigDecimal;
import java.util.List;

/**
 * Command to create a subscription plan.
 */
public record CreateSubscriptionPlanCommand(String code, String name, String description, BigDecimal amount,
                                            BigDecimal annualAmount, String currency, BillingPeriod billingPeriod,
                                            List<String> features, Boolean active) {
}
