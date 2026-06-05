package online.entreprenly.platform.subscription.application.internal.eventhandlers;

import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds the subscription catalog expected by the frontend.
 */
@Component
@Slf4j
public class SubscriptionCatalogReadyEventHandler {

    public static final String FREE_PLAN_CODE = "plan-free";
    public static final String CONTROL_PLAN_CODE = "plan-control";

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionCatalogReadyEventHandler(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        ensureDefaultPlans();
    }

    public void ensureDefaultPlans() {
        ensurePlan(FREE_PLAN_CODE, "Plan Free", "Inventario básico para empezar sin costo.",
                BigDecimal.ZERO, BigDecimal.ZERO, List.of(
                        "basic-inventory",
                        "manual-movements"));
        ensurePlan(CONTROL_PLAN_CODE, "Plan Control", "Opera sin restricciones con automatizaciones, alertas y trazabilidad completa.",
                BigDecimal.valueOf(89), BigDecimal.valueOf(890), List.of(
                        "unlimited-products",
                        "sales-operations",
                        "chatbot"));
    }

    private void ensurePlan(String code, String name, String description, BigDecimal monthlyAmount,
                            BigDecimal annualAmount, List<String> features) {
        if (subscriptionPlanRepository.existsByCode(code)) {
            return;
        }
        var plan = new SubscriptionPlan(
                code,
                name,
                description,
                new Money(monthlyAmount, "PEN"),
                new Money(annualAmount, "PEN"),
                BillingPeriod.MONTHLY,
                features,
                true);
        subscriptionPlanRepository.save(plan);
        log.info("Seeded subscription plan {}", code);
    }
}
