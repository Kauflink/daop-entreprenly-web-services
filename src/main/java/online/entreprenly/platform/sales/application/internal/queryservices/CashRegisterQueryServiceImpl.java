package online.entreprenly.platform.sales.application.internal.queryservices;

import online.entreprenly.platform.sales.application.queryservices.CashRegisterQueryService;
import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;
import online.entreprenly.platform.sales.domain.model.queries.GetAllCashRegistersQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetCashRegisterByDateQuery;
import online.entreprenly.platform.sales.domain.repositories.CashRegisterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves cash register read queries.
 */
@Service
public class CashRegisterQueryServiceImpl implements CashRegisterQueryService {

    private final CashRegisterRepository cashRegisterRepository;

    public CashRegisterQueryServiceImpl(CashRegisterRepository cashRegisterRepository) {
        this.cashRegisterRepository = cashRegisterRepository;
    }

    @Override
    public List<CashRegister> handle(GetAllCashRegistersQuery query) {
        return cashRegisterRepository.findAll();
    }

    @Override
    public Optional<CashRegister> handle(GetCashRegisterByDateQuery query) {
        return cashRegisterRepository.findByDate(query.date());
    }
}
