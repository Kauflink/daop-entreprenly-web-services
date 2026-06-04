package online.entreprenly.platform.sales.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities.SalePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for sale persistence entities.
 */
@Repository
public interface SalePersistenceRepository extends JpaRepository<SalePersistenceEntity, Long> {
}
