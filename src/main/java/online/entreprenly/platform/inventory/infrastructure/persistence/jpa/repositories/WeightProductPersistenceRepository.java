package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.WeightProductPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for weight product persistence entities.
 */
@Repository
public interface WeightProductPersistenceRepository extends JpaRepository<WeightProductPersistenceEntity, Long> {
    boolean existsByCodeQR(String codeQR);
}
