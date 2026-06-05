package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionRepository;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers.SubscriptionPersistenceAssembler;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository adapter for subscriptions.
 */
@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionPersistenceRepository persistenceRepository;

    public SubscriptionRepositoryImpl(SubscriptionPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return persistenceRepository.findById(id).map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Subscription> findFirstByUserIdAndStatus(Long userId, SubscriptionStatus status) {
        return persistenceRepository.findFirstByUserIdAndStatusOrderByStartedAtDesc(userId, status)
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Subscription save(Subscription subscription) {
        var saved = persistenceRepository.save(SubscriptionPersistenceAssembler.toPersistenceFromDomain(subscription));
        return SubscriptionPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
