package online.entreprenly.platform.sales.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities.SalePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for sale persistence entities, scoped by owner account.
 */
@Repository
public interface SalePersistenceRepository extends JpaRepository<SalePersistenceEntity, Long> {
    List<SalePersistenceEntity> findAllByOwnerEmail(String ownerEmail);

    List<SalePersistenceEntity> findAllByOwnerEmailAndSaleCreatedAtGreaterThanEqualAndSaleCreatedAtLessThan(
            String ownerEmail, Instant start, Instant end);

    Optional<SalePersistenceEntity> findByIdAndOwnerEmail(Long id, String ownerEmail);
}
