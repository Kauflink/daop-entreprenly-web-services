package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.UnitLotPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for unit lot persistence entities.
 */
@Repository
public interface UnitLotPersistenceRepository extends JpaRepository<UnitLotPersistenceEntity, Long> {
}
