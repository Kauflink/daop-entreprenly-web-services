package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.repositories.WeightLotRepository;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers.WeightLotPersistenceAssembler;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories.WeightLotPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the weight lot domain repository port with Spring Data JPA.
 */
@Repository
public class WeightLotRepositoryImpl implements WeightLotRepository {

    private final WeightLotPersistenceRepository weightLotPersistenceRepository;

    public WeightLotRepositoryImpl(WeightLotPersistenceRepository weightLotPersistenceRepository) {
        this.weightLotPersistenceRepository = weightLotPersistenceRepository;
    }

    @Override
    public List<WeightLot> findAll() {
        return weightLotPersistenceRepository.findAll().stream()
                .map(WeightLotPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<WeightLot> findById(Long id) {
        return weightLotPersistenceRepository.findById(id)
                .map(WeightLotPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsById(Long id) {
        return weightLotPersistenceRepository.existsById(id);
    }

    @Override
    public WeightLot save(WeightLot weightLot) {
        var saved = weightLotPersistenceRepository.save(WeightLotPersistenceAssembler.toPersistenceFromDomain(weightLot));
        return WeightLotPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(Long id) {
        weightLotPersistenceRepository.deleteById(id);
    }
}
