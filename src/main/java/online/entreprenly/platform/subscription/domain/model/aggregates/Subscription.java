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
    private BillingPeriod billingPeriod;

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
        this.billingPeriod = BillingPeriod.MONTHLY;
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
        this.billingPeriod = billingPeriod == null ? BillingPeriod.MONTHLY : billingPeriod;
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodEnd = calculatePeriodEnd(Instant.now(), this.billingPeriod);
        this.cancelledAt = null;
    }

    public void activateWithoutExpiration() {
        this.billingPeriod = BillingPeriod.MONTHLY;
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodEnd = null;
        this.cancelledAt = null;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = Instant.now();
    }

    /**
     * Marks the subscription for cancellation at the end of the current period while keeping it
     * active so the user retains access until {@code currentPeriodEnd}.
     */
    public void scheduleCancellation() {
        this.status = SubscriptionStatus.ACTIVE;
        this.cancelledAt = Instant.now();
    }

    /**
     * Reverts a scheduled cancellation, keeping the subscription active without resetting the
     * current billing period.
     */
    public void resumeActive() {
        this.status = SubscriptionStatus.ACTIVE;
        this.cancelledAt = null;
    }

    public boolean isCancellationScheduled() {
        return status == SubscriptionStatus.ACTIVE && cancelledAt != null;
    }

    public boolean isActiveAt(Instant instant) {
        return status == SubscriptionStatus.ACTIVE
                && (currentPeriodEnd == null || currentPeriodEnd.isAfter(instant));
    }

    public void restoreState(Long id, Long userId, Long planId, SubscriptionStatus status, Instant startedAt,
                             Instant currentPeriodEnd, Instant cancelledAt, Long latestPaymentId,
                             BillingPeriod billingPeriod) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.startedAt = startedAt;
        this.currentPeriodEnd = currentPeriodEnd;
        this.cancelledAt = cancelledAt;
        this.latestPaymentId = latestPaymentId;
        this.billingPeriod = billingPeriod == null ? BillingPeriod.MONTHLY : billingPeriod;
    }

    private static Instant calculatePeriodEnd(Instant start, BillingPeriod billingPeriod) {
        return switch (billingPeriod == null ? BillingPeriod.MONTHLY : billingPeriod) {
            case MONTHLY -> start.plus(Duration.ofDays(30));
            case YEARLY -> start.plus(Duration.ofDays(365));
        };
    }
}
