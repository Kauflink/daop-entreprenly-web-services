package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;

/**
 * Static assembler between subscription domain and persistence representations.
 */
public final class SubscriptionPersistenceAssembler {

    private SubscriptionPersistenceAssembler() {
    }

    public static Subscription toDomainFromPersistence(SubscriptionPersistenceEntity entity) {
        if (entity == null) return null;
        var subscription = new Subscription();
        subscription.restoreState(
                entity.getId(),
                entity.getUserId(),
                entity.getPlanId(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getCurrentPeriodEnd(),
                entity.getCancelledAt(),
                entity.getLatestPaymentId(),
                entity.getBillingPeriod());
        return subscription;
    }

    public static SubscriptionPersistenceEntity toPersistenceFromDomain(Subscription subscription) {
        if (subscription == null) return null;
        var entity = new SubscriptionPersistenceEntity();
        if (subscription.getId() != null) {
            entity.setId(subscription.getId());
        }
        entity.setUserId(subscription.getUserId());
        entity.setPlanId(subscription.getPlanId());
        entity.setStatus(subscription.getStatus());
        entity.setStartedAt(subscription.getStartedAt());
        entity.setCurrentPeriodEnd(subscription.getCurrentPeriodEnd());
        entity.setCancelledAt(subscription.getCancelledAt());
        entity.setLatestPaymentId(subscription.getLatestPaymentId());
        entity.setBillingPeriod(subscription.getBillingPeriod());
        return entity;
    }
}
