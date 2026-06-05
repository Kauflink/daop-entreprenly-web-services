package online.entreprenly.platform.subscription.infrastructure.payments.fake;

import online.entreprenly.platform.subscription.application.internal.outboundservices.payments.PaymentGateway;
import online.entreprenly.platform.subscription.application.internal.outboundservices.payments.PaymentGatewayRequest;
import online.entreprenly.platform.subscription.application.internal.outboundservices.payments.PaymentGatewayResponse;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Fake payment gateway used instead of a real provider.
 */
@Service
public class FakePaymentGateway implements PaymentGateway {

    @Override
    public PaymentGatewayResponse process(PaymentGatewayRequest request) {
        var status = request.requestedStatus() == null ? PaymentStatus.APPROVED : request.requestedStatus();
        var transactionId = "fake_tx_%s".formatted(UUID.randomUUID().toString().replace("-", ""));
        var message = switch (status) {
            case APPROVED -> "Fake payment approved";
            case DECLINED -> "Fake payment declined";
            case FAILED -> "Fake payment failed";
            case PENDING -> "Fake payment pending";
        };
        return new PaymentGatewayResponse(transactionId, status, message);
    }
}
