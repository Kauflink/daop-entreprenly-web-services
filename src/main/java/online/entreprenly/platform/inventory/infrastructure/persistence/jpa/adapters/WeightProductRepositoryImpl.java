package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.domain.repositories.WeightProductRepository;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers.WeightProductPersistenceAssembler;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories.WeightProductPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the weight product domain repository port with Spring Data JPA.
 */
@Repository
public class WeightProductRepositoryImpl implements WeightProductRepository {

    private final WeightProductPersistenceRepository weightProductPersistenceRepository;

    public WeightProductRepositoryImpl(WeightProductPersistenceRepository weightProductPersistenceRepository) {
        this.weightProductPersistenceRepository = weightProductPersistenceRepository;
    }

    @Override
    public List<WeightProduct> findAll() {
        return weightProductPersistenceRepository.findAll().stream()
                .map(WeightProductPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<WeightProduct> findById(Long id) {
        return weightProductPersistenceRepository.findById(id)
                .map(WeightProductPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsById(Long id) {
        return weightProductPersistenceRepository.existsById(id);
    }

    @Override
    public boolean existsByCodeQR(String codeQR) {
        return weightProductPersistenceRepository.existsByCodeQR(codeQR);
    }

    @Override
    public WeightProduct save(WeightProduct weightProduct) {
        var saved = weightProductPersistenceRepository.save(
                WeightProductPersistenceAssembler.toPersistenceFromDomain(weightProduct));
        return WeightProductPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(Long id) {
        weightProductPersistenceRepository.deleteById(id);
    }
}
