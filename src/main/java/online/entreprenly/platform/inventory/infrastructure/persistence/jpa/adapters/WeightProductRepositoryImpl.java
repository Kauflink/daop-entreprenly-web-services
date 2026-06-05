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
    public List<WeightProduct> findAllByOwnerEmail(String ownerEmail) {
        return weightProductPersistenceRepository.findAllByOwnerEmail(ownerEmail).stream()
                .map(WeightProductPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<WeightProduct> findByIdAndOwnerEmail(Long id, String ownerEmail) {
        return weightProductPersistenceRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .map(WeightProductPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByIdAndOwnerEmail(Long id, String ownerEmail) {
        return weightProductPersistenceRepository.existsByIdAndOwnerEmail(id, ownerEmail);
    }

    @Override
    public boolean existsByCodeQRAndOwnerEmail(String codeQR, String ownerEmail) {
        return weightProductPersistenceRepository.existsByCodeQRAndOwnerEmail(codeQR, ownerEmail);
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
