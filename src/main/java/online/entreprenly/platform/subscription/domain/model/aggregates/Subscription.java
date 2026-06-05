package online.entreprenly.platform.subscription.domain.model.aggregates;

import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

/**
 * User subscription aggregate root.
 */
@Getter
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {

    @Setter
    private Long id;
    private Long userId;
    private Long planId;
    private SubscriptionStatus status;
    private Instant startedAt;
    private Instant currentPeriodEnd;
    private Instant cancelledAt;
    private Long latestPaymentId;

    public Subscription() {
    }

    public Subscription(Long userId, SubscriptionPlan plan) {
        this.userId = userId;
        this.planId = plan.getId();
        this.status = SubscriptionStatus.PENDING_PAYMENT;
        this.startedAt = Instant.now();
        this.currentPeriodEnd = null;
        this.cancelledAt = null;
        this.latestPaymentId = null;
    }

    public void applyPayment(Payment payment, BillingPeriod billingPeriod) {
        this.latestPaymentId = payment.getId();
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            activate(billingPeriod);
            return;
        }
        if (payment.getStatus() == PaymentStatus.PENDING) {
            this.status = SubscriptionStatus.PENDING_PAYMENT;
            return;
        }
        this.status = SubscriptionStatus.PENDING_PAYMENT;
    }

    public void activate(BillingPeriod billingPeriod) {
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodEnd = calculatePeriodEnd(Instant.now(), billingPeriod);
        this.cancelledAt = null;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = Instant.now();
    }

    public boolean isActiveAt(Instant instant) {
        return status == SubscriptionStatus.ACTIVE
                && currentPeriodEnd != null
                && currentPeriodEnd.isAfter(instant);
    }

    public void restoreState(Long id, Long userId, Long planId, SubscriptionStatus status, Instant startedAt,
                             Instant currentPeriodEnd, Instant cancelledAt, Long latestPaymentId) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.startedAt = startedAt;
        this.currentPeriodEnd = currentPeriodEnd;
        this.cancelledAt = cancelledAt;
        this.latestPaymentId = latestPaymentId;
    }

    private static Instant calculatePeriodEnd(Instant start, BillingPeriod billingPeriod) {
        return switch (billingPeriod == null ? BillingPeriod.MONTHLY : billingPeriod) {
            case MONTHLY -> start.plus(Duration.ofDays(30));
            case YEARLY -> start.plus(Duration.ofDays(365));
        };
    }
}
