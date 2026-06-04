package online.entreprenly.platform.sales.domain.repositories;

import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Cash register repository port.
 */
public interface CashRegisterRepository {
    List<CashRegister> findAll();

    Optional<CashRegister> findById(Long id);

    Optional<CashRegister> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);

    CashRegister save(CashRegister cashRegister);
}
