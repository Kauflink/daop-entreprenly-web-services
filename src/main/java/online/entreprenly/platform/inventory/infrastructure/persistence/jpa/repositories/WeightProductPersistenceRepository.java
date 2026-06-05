package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.WeightProductPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for weight product persistence entities, scoped by owner account.
 */
@Repository
public interface WeightProductPersistenceRepository extends JpaRepository<WeightProductPersistenceEntity, Long> {
    List<WeightProductPersistenceEntity> findAllByOwnerEmail(String ownerEmail);

    Optional<WeightProductPersistenceEntity> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByCodeQRAndOwnerEmail(String codeQR, String ownerEmail);
}
