package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers.UnitLotPersistenceAssembler;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories.UnitLotPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the unit lot domain repository port with Spring Data JPA.
 */
@Repository
public class UnitLotRepositoryImpl implements UnitLotRepository {

    private final UnitLotPersistenceRepository unitLotPersistenceRepository;

    public UnitLotRepositoryImpl(UnitLotPersistenceRepository unitLotPersistenceRepository) {
        this.unitLotPersistenceRepository = unitLotPersistenceRepository;
    }

    @Override
    public List<UnitLot> findAll() {
        return unitLotPersistenceRepository.findAll().stream()
                .map(UnitLotPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<UnitLot> findById(Long id) {
        return unitLotPersistenceRepository.findById(id)
                .map(UnitLotPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsById(Long id) {
        return unitLotPersistenceRepository.existsById(id);
    }

    @Override
    public UnitLot save(UnitLot unitLot) {
        var saved = unitLotPersistenceRepository.save(UnitLotPersistenceAssembler.toPersistenceFromDomain(unitLot));
        return UnitLotPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(Long id) {
        unitLotPersistenceRepository.deleteById(id);
    }
}
