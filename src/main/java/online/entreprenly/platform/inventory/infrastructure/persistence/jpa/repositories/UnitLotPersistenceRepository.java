package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.UnitLotPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for unit lot persistence entities, scoped by owner account.
 */
@Repository
public interface UnitLotPersistenceRepository extends JpaRepository<UnitLotPersistenceEntity, Long> {
    List<UnitLotPersistenceEntity> findAllByOwnerEmail(String ownerEmail);

    List<UnitLotPersistenceEntity> findAllByProductIdAndOwnerEmail(Long productId, String ownerEmail);

    Optional<UnitLotPersistenceEntity> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);
}
