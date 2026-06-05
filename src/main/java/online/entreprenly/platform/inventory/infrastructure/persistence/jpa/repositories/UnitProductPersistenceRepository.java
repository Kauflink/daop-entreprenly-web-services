package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.UnitProductPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for unit product persistence entities, scoped by owner account.
 */
@Repository
public interface UnitProductPersistenceRepository extends JpaRepository<UnitProductPersistenceEntity, Long> {
    List<UnitProductPersistenceEntity> findAllByOwnerEmail(String ownerEmail);

    Optional<UnitProductPersistenceEntity> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByCodeQRAndOwnerEmail(String codeQR, String ownerEmail);
}
