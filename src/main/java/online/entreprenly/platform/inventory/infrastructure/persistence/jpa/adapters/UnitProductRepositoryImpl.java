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
    public List<UnitProduct> findAllByOwnerEmail(String ownerEmail) {
        return unitProductPersistenceRepository.findAllByOwnerEmail(ownerEmail).stream()
                .map(UnitProductPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<UnitProduct> findByIdAndOwnerEmail(Long id, String ownerEmail) {
        return unitProductPersistenceRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .map(UnitProductPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByIdAndOwnerEmail(Long id, String ownerEmail) {
        return unitProductPersistenceRepository.existsByIdAndOwnerEmail(id, ownerEmail);
    }

    @Override
    public boolean existsByCodeQRAndOwnerEmail(String codeQR, String ownerEmail) {
        return unitProductPersistenceRepository.existsByCodeQRAndOwnerEmail(codeQR, ownerEmail);
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
