package online.entreprenly.platform.subscription.domain.model.aggregates;

import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Payment aggregate root for subscription billing attempts.
 */
@Getter
public class Payment extends AbstractDomainAggregateRoot<Payment> {

    @Setter
    private Long id;
    private Long subscriptionId;
    private Long userId;
    private Long planId;
    private Money amount;
    private String paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private String providerMessage;
    private PaymentStatus requestedStatus;
    private online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod billingPeriod;
    private Instant requestedAt;
    private Instant processedAt;

    public Payment() {
    }

    public Payment(Long subscriptionId, Long userId, Long planId, Money amount, String paymentMethod,
                   PaymentStatus status, String transactionId, String providerMessage,
                   PaymentStatus requestedStatus,
                   online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod billingPeriod) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.planId = planId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionId = transactionId;
        this.providerMessage = providerMessage;
        this.requestedStatus = requestedStatus;
        this.billingPeriod = billingPeriod == null
                ? online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod.MONTHLY
                : billingPeriod;
        this.requestedAt = Instant.now();
        this.processedAt = status == PaymentStatus.PENDING ? null : this.requestedAt;
    }

    public void restoreState(Long id, Long subscriptionId, Long userId, Long planId, Money amount,
                             String paymentMethod, PaymentStatus status, String transactionId,
                             String providerMessage, PaymentStatus requestedStatus,
                             online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod billingPeriod,
                             Instant requestedAt,
                             Instant processedAt) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.planId = planId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionId = transactionId;
        this.providerMessage = providerMessage;
        this.requestedStatus = requestedStatus;
        this.billingPeriod = billingPeriod == null
                ? online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod.MONTHLY
                : billingPeriod;
        this.requestedAt = requestedAt;
        this.processedAt = processedAt;
    }
}
