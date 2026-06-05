package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.repositories.UnitProductRepository;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers.UnitProductPersistenceAssembler;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories.UnitProductPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the unit product domain repository port with Spring Data JPA.
 */
@Repository
public class UnitProductRepositoryImpl implements UnitProductRepository {

    private final UnitProductPersistenceRepository unitProductPersistenceRepository;

    public UnitProductRepositoryImpl(UnitProductPersistenceRepository unitProductPersistenceRepository) {
        this.unitProductPersistenceRepository = unitProductPersistenceRepository;
    }

    @Override
    public List<UnitProduct> findAll() {
        return unitProductPersistenceRepository.findAll().stream()
                .map(UnitProductPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<UnitProduct> findById(Long id) {
        return unitProductPersistenceRepository.findById(id)
                .map(UnitProductPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsById(Long id) {
        return unitProductPersistenceRepository.existsById(id);
    }

    @Override
    public boolean existsByCodeQR(String codeQR) {
        return unitProductPersistenceRepository.existsByCodeQR(codeQR);
    }

    @Override
    public UnitProduct save(UnitProduct unitProduct) {
        var saved = unitProductPersistenceRepository.save(
                UnitProductPersistenceAssembler.toPersistenceFromDomain(unitProduct));
        return UnitProductPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(Long id) {
        unitProductPersistenceRepository.deleteById(id);
    }
}
