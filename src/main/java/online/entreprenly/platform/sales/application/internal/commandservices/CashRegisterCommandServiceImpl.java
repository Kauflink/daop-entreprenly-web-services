package online.entreprenly.platform.sales.application.internal.commandservices;

import online.entreprenly.platform.sales.application.commandservices.CashRegisterCommandService;
import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;
import online.entreprenly.platform.sales.domain.model.commands.CreateCashRegisterCommand;
import online.entreprenly.platform.sales.domain.model.commands.UpdateCashRegisterCommand;
import online.entreprenly.platform.sales.domain.repositories.CashRegisterRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Cash register command service implementation.
 */
@Service
public class CashRegisterCommandServiceImpl implements CashRegisterCommandService {

    private final CashRegisterRepository cashRegisterRepository;

    public CashRegisterCommandServiceImpl(CashRegisterRepository cashRegisterRepository) {
        this.cashRegisterRepository = cashRegisterRepository;
    }

    @Override
    public Result<CashRegister, ApplicationError> handle(CreateCashRegisterCommand command) {
        if (command.date() == null) {
            return Result.failure(ApplicationError.validationError("date", "A business day is required"));
        }
        if (cashRegisterRepository.existsByDate(command.date())) {
            return Result.failure(ApplicationError.conflict("CashRegister",
                    "A cash register already exists for this day"));
        }
        var cashRegister = new CashRegister(command.date(), command.totalCash(), command.totalDigital(), 0);
        return Result.success(cashRegisterRepository.save(cashRegister));
    }

    @Override
    public Result<CashRegister, ApplicationError> handle(UpdateCashRegisterCommand command) {
        return cashRegisterRepository.findById(command.cashRegisterId())
                .<Result<CashRegister, ApplicationError>>map(cashRegister -> {
                    cashRegister.updateTotals(command.totalCash(), command.totalDigital(), command.saleCount());
                    return Result.success(cashRegisterRepository.save(cashRegister));
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("CashRegister", String.valueOf(command.cashRegisterId()))));
    }
}
