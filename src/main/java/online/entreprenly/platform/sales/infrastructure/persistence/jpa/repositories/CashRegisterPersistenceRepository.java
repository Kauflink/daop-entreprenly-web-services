package online.entreprenly.platform.sales.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities.CashRegisterPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data repository for cash register persistence entities.
 */
@Repository
public interface CashRegisterPersistenceRepository extends JpaRepository<CashRegisterPersistenceEntity, Long> {

    /**
     * Finds the cash register that belongs to a given business day.
     *
     * @param date the business day
     * @return the cash register, if present
     */
    Optional<CashRegisterPersistenceEntity> findByDate(LocalDate date);

    /**
     * Checks whether a cash register already exists for the given business day.
     *
     * @param date the business day
     * @return {@code true} if a cash register exists, otherwise {@code false}
     */
    boolean existsByDate(LocalDate date);
}
