package online.entreprenly.platform.subscription.application.internal.outboundservices.payments;

/**
 * Payment processing port used by subscription application services.
 */
public interface PaymentGateway {
    PaymentGatewayResponse process(PaymentGatewayRequest request);
}
