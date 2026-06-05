package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.subscription.domain.model.aggregates.Payment;
import online.entreprenly.platform.subscription.domain.repositories.PaymentRepository;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers.PaymentPersistenceAssembler;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.repositories.PaymentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository adapter for fake subscription payments.
 */
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentPersistenceRepository persistenceRepository;

    public PaymentRepositoryImpl(PaymentPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<Payment> findBySubscriptionId(Long subscriptionId) {
        return persistenceRepository.findBySubscriptionIdOrderByRequestedAtDesc(subscriptionId).stream()
                .map(PaymentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Payment save(Payment payment) {
        var saved = persistenceRepository.save(PaymentPersistenceAssembler.toPersistenceFromDomain(payment));
        return PaymentPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
