package online.entreprenly.platform.sales.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.repositories.SaleRepository;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.assemblers.SalePersistenceAssembler;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.repositories.SalePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the sale domain repository port with Spring Data JPA.
 */
@Repository
public class SaleRepositoryImpl implements SaleRepository {

    private final SalePersistenceRepository salePersistenceRepository;

    public SaleRepositoryImpl(SalePersistenceRepository salePersistenceRepository) {
        this.salePersistenceRepository = salePersistenceRepository;
    }

    @Override
    public List<Sale> findAllByOwnerEmail(String ownerEmail) {
        return salePersistenceRepository.findAllByOwnerEmail(ownerEmail).stream()
                .map(SalePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Sale> findByIdAndOwnerEmail(Long id, String ownerEmail) {
        return salePersistenceRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .map(SalePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Sale save(Sale sale) {
        var saved = salePersistenceRepository.save(SalePersistenceAssembler.toPersistenceFromDomain(sale));
        return SalePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
