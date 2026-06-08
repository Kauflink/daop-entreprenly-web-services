package online.entreprenly.platform.sales.domain.repositories;

import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Cash register repository port. All lookups are scoped to the owner account.
 */
public interface CashRegisterRepository {
    List<CashRegister> findAllByOwnerEmail(String ownerEmail);

    Optional<CashRegister> findByIdAndOwnerEmail(Long id, String ownerEmail);

    Optional<CashRegister> findByDateAndOwnerEmail(LocalDate date, String ownerEmail);

    boolean existsByDateAndOwnerEmail(LocalDate date, String ownerEmail);

    CashRegister save(CashRegister cashRegister);
}
