package online.entreprenly.platform.subscription.domain.model.aggregates;

import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTests {

    @Test
    void approvedPaymentActivatesSubscription() {
        var plan = plan();
        var subscription = new Subscription(1L, plan);
        subscription.setId(1L);
        var payment = new Payment(1L, 1L, 1L, plan.getPrice(), "FAKE_CARD",
                PaymentStatus.APPROVED, "fake_tx_1", "Fake payment approved", PaymentStatus.APPROVED);
        payment.setId(1L);

        subscription.applyPayment(payment, plan.getBillingPeriod());

        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
        assertEquals(1L, subscription.getLatestPaymentId());
        assertTrue(subscription.isActiveAt(Instant.now()));
    }

    @Test
    void declinedPaymentLeavesSubscriptionPendingPayment() {
        var plan = plan();
        var subscription = new Subscription(1L, plan);
        subscription.setId(1L);
        var payment = new Payment(1L, 1L, 1L, plan.getPrice(), "FAKE_CARD",
                PaymentStatus.DECLINED, "fake_tx_2", "Fake payment declined", PaymentStatus.DECLINED);
        payment.setId(2L);

        subscription.applyPayment(payment, plan.getBillingPeriod());

        assertEquals(SubscriptionStatus.PENDING_PAYMENT, subscription.getStatus());
        assertEquals(2L, subscription.getLatestPaymentId());
    }

    private static SubscriptionPlan plan() {
        var plan = new SubscriptionPlan("Premium", "Premium plan", new Money(BigDecimal.valueOf(49.90), "USD"),
                BillingPeriod.MONTHLY, List.of("dashboard"), true);
        plan.setId(1L);
        return plan;
    }
}
