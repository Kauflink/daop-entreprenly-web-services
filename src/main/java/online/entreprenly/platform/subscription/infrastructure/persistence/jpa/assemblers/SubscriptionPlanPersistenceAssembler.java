package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;

/**
 * Static assembler between subscription plan domain and persistence representations.
 */
public final class SubscriptionPlanPersistenceAssembler {

    private SubscriptionPlanPersistenceAssembler() {
    }

    public static SubscriptionPlan toDomainFromPersistence(SubscriptionPlanPersistenceEntity entity) {
        if (entity == null) return null;
        var plan = new SubscriptionPlan();
        plan.restoreState(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                new Money(entity.getAmount(), entity.getCurrency()),
                new Money(entity.getAnnualAmount(), entity.getCurrency()),
                entity.getBillingPeriod(),
                entity.getFeatures(),
                entity.isActive());
        return plan;
    }

    public static SubscriptionPlanPersistenceEntity toPersistenceFromDomain(SubscriptionPlan plan) {
        if (plan == null) return null;
        var entity = new SubscriptionPlanPersistenceEntity();
        if (plan.getId() != null) {
            entity.setId(plan.getId());
        }
        entity.setCode(plan.getCode());
        entity.setName(plan.getName());
        entity.setDescription(plan.getDescription());
        entity.setAmount(plan.getPrice().amount());
        entity.setAnnualAmount(plan.getAnnualPrice().amount());
        entity.setCurrency(plan.getPrice().currency());
        entity.setBillingPeriod(plan.getBillingPeriod());
        entity.setFeatures(new java.util.ArrayList<>(plan.getFeatures()));
        entity.setActive(plan.isActive());
        return entity;
    }
}
