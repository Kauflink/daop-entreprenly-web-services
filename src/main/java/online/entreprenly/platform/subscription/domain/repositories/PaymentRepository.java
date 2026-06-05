package online.entreprenly.platform.subscription.domain.repositories;

import online.entreprenly.platform.subscription.domain.model.aggregates.Payment;

import java.util.List;

/**
 * Payment repository port.
 */
public interface PaymentRepository {
    List<Payment> findBySubscriptionId(Long subscriptionId);
    Payment save(Payment payment);
}
