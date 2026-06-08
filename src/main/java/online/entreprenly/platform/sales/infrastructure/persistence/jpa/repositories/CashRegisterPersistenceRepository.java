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
     * Lists the cash registers owned by an account.
     *
     * @param ownerEmail the owner account email
     * @return the account's cash registers
     */
    java.util.List<CashRegisterPersistenceEntity> findAllByOwnerEmail(String ownerEmail);

    /**
     * Finds a cash register by its identifier within an owner account.
     *
     * @param id         the cash register identifier
     * @param ownerEmail the owner account email
     * @return the cash register, if present
     */
    Optional<CashRegisterPersistenceEntity> findByIdAndOwnerEmail(Long id, String ownerEmail);

    /**
     * Finds the cash register that belongs to a given business day for an owner account.
     *
     * @param date       the business day
     * @param ownerEmail the owner account email
     * @return the cash register, if present
     */
    Optional<CashRegisterPersistenceEntity> findByDateAndOwnerEmail(LocalDate date, String ownerEmail);

    /**
     * Checks whether a cash register already exists for the given business day within an
     * owner account.
     *
     * @param date       the business day
     * @param ownerEmail the owner account email
     * @return {@code true} if a cash register exists, otherwise {@code false}
     */
    boolean existsByDateAndOwnerEmail(LocalDate date, String ownerEmail);
}
