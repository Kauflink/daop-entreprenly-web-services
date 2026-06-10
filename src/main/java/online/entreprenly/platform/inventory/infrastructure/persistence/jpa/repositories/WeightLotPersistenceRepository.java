package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.WeightLotPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for weight lot persistence entities, scoped by owner account.
 */
@Repository
public interface WeightLotPersistenceRepository extends JpaRepository<WeightLotPersistenceEntity, Long> {
    List<WeightLotPersistenceEntity> findAllByOwnerEmail(String ownerEmail);

    List<WeightLotPersistenceEntity> findAllByProductIdAndOwnerEmail(Long productId, String ownerEmail);

    Optional<WeightLotPersistenceEntity> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);
}
