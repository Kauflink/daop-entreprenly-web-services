package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers.SubscriptionPlanPersistenceAssembler;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.repositories.SubscriptionPlanPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for subscription plans.
 */
@Repository
public class SubscriptionPlanRepositoryImpl implements SubscriptionPlanRepository {

    private final SubscriptionPlanPersistenceRepository persistenceRepository;

    public SubscriptionPlanRepositoryImpl(SubscriptionPlanPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<SubscriptionPlan> findAll() {
        return persistenceRepository.findAll().stream()
                .map(SubscriptionPlanPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<SubscriptionPlan> findById(Long id) {
        return persistenceRepository.findById(id).map(SubscriptionPlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<SubscriptionPlan> findByCode(String code) {
        return persistenceRepository.findByCode(code).map(SubscriptionPlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return persistenceRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return persistenceRepository.existsByCode(code);
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan plan) {
        var saved = persistenceRepository.save(SubscriptionPlanPersistenceAssembler.toPersistenceFromDomain(plan));
        return SubscriptionPlanPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
