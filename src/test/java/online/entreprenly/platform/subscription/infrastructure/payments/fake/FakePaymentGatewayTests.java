package online.entreprenly.platform.subscription.infrastructure.payments.fake;

import online.entreprenly.platform.subscription.application.internal.outboundservices.payments.PaymentGatewayRequest;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FakePaymentGatewayTests {

    @Test
    void processUsesRequestedPaymentStatus() {
        var gateway = new FakePaymentGateway();
        var request = new PaymentGatewayRequest(1L, 1L, 1L, new Money(BigDecimal.TEN, "USD"),
                "FAKE_CARD", "fake-token", PaymentStatus.DECLINED, BillingPeriod.MONTHLY);

        var response = gateway.process(request);

        assertEquals(PaymentStatus.DECLINED, response.status());
        assertTrue(response.transactionId().startsWith("fake_tx_"));
    }

    @Test
    void processDefaultsToApprovedWhenNoStatusRequested() {
        var gateway = new FakePaymentGateway();
        var request = new PaymentGatewayRequest(1L, 1L, 1L, new Money(BigDecimal.TEN, "USD"),
                "FAKE_CARD", "fake-token", null, BillingPeriod.MONTHLY);

        var response = gateway.process(request);

        assertEquals(PaymentStatus.APPROVED, response.status());
    }
}
