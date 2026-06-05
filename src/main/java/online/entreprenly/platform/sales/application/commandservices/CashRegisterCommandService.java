package online.entreprenly.platform.sales.application.commandservices;

import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;
import online.entreprenly.platform.sales.domain.model.commands.CreateCashRegisterCommand;
import online.entreprenly.platform.sales.domain.model.commands.UpdateCashRegisterCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service contract for cash register commands.
 */
public interface CashRegisterCommandService {
    /**
     * Opens a cash register for a business day.
     *
     * @param command create command
     * @return created cash register, or an application error
     */
    Result<CashRegister, ApplicationError> handle(CreateCashRegisterCommand command);

    /**
     * Updates the running totals of a cash register.
     *
     * @param command update command
     * @return updated cash register, or an application error
     */
    Result<CashRegister, ApplicationError> handle(UpdateCashRegisterCommand command);
}
