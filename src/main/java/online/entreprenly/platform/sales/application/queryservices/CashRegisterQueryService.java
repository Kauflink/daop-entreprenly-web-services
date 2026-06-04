package online.entreprenly.platform.sales.application.queryservices;

import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;
import online.entreprenly.platform.sales.domain.model.queries.GetAllCashRegistersQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetCashRegisterByDateQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for cash register read queries.
 */
public interface CashRegisterQueryService {
    /**
     * Handles retrieval of every cash register.
     *
     * @param query get-all query
     * @return the cash registers
     */
    List<CashRegister> handle(GetAllCashRegistersQuery query);

    /**
     * Handles retrieval of the cash register that belongs to a business day.
     *
     * @param query date query
     * @return matching cash register, if found
     */
    Optional<CashRegister> handle(GetCashRegisterByDateQuery query);
}
