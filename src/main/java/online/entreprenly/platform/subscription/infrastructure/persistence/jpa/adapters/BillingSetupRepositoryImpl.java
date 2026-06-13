package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.subscription.domain.model.aggregates.BillingSetup;
import online.entreprenly.platform.subscription.domain.repositories.BillingSetupRepository;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers.BillingSetupPersistenceAssembler;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.repositories.BillingSetupPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository adapter for billing setups.
 */
@Repository
public class BillingSetupRepositoryImpl implements BillingSetupRepository {

    private final BillingSetupPersistenceRepository persistenceRepository;

    public BillingSetupRepositoryImpl(BillingSetupPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<BillingSetup> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId)
                .map(BillingSetupPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public BillingSetup save(BillingSetup billingSetup) {
        var saved = persistenceRepository.save(
                BillingSetupPersistenceAssembler.toPersistenceFromDomain(billingSetup));
        return BillingSetupPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
