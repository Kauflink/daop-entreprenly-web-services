package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.WeightLotPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for weight lot persistence entities.
 */
@Repository
public interface WeightLotPersistenceRepository extends JpaRepository<WeightLotPersistenceEntity, Long> {
}
