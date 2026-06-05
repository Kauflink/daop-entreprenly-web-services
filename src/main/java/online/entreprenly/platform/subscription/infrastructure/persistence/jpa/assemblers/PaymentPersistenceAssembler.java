package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.subscription.domain.model.aggregates.Payment;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;

/**
 * Static assembler between payment domain and persistence representations.
 */
public final class PaymentPersistenceAssembler {

    private PaymentPersistenceAssembler() {
    }

    public static Payment toDomainFromPersistence(PaymentPersistenceEntity entity) {
        if (entity == null) return null;
        var payment = new Payment();
        payment.restoreState(
                entity.getId(),
                entity.getSubscriptionId(),
                entity.getUserId(),
                entity.getPlanId(),
                new Money(entity.getAmount(), entity.getCurrency()),
                entity.getPaymentMethod(),
                entity.getStatus(),
                entity.getTransactionId(),
                entity.getProviderMessage(),
                entity.getRequestedStatus(),
                entity.getBillingPeriod(),
                entity.getRequestedAt(),
                entity.getProcessedAt());
        return payment;
    }

    public static PaymentPersistenceEntity toPersistenceFromDomain(Payment payment) {
        if (payment == null) return null;
        var entity = new PaymentPersistenceEntity();
        if (payment.getId() != null) {
            entity.setId(payment.getId());
        }
        entity.setSubscriptionId(payment.getSubscriptionId());
        entity.setUserId(payment.getUserId());
        entity.setPlanId(payment.getPlanId());
        entity.setAmount(payment.getAmount().amount());
        entity.setCurrency(payment.getAmount().currency());
        entity.setPaymentMethod(payment.getPaymentMethod());
        entity.setStatus(payment.getStatus());
        entity.setTransactionId(payment.getTransactionId());
        entity.setProviderMessage(payment.getProviderMessage());
        entity.setRequestedStatus(payment.getRequestedStatus());
        entity.setBillingPeriod(payment.getBillingPeriod());
        entity.setRequestedAt(payment.getRequestedAt());
        entity.setProcessedAt(payment.getProcessedAt());
        return entity;
    }
}
