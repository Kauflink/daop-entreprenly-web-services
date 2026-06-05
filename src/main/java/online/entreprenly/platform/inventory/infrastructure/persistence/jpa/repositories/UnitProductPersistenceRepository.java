package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.UnitProductPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for unit product persistence entities.
 */
@Repository
public interface UnitProductPersistenceRepository extends JpaRepository<UnitProductPersistenceEntity, Long> {
    boolean existsByCodeQR(String codeQR);
}
