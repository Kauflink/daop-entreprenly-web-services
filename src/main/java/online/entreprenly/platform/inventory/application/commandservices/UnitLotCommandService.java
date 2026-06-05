package online.entreprenly.platform.inventory.application.commandservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteUnitLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateUnitLotCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service contract for unit lot commands.
 */
public interface UnitLotCommandService {
    Result<UnitLot, ApplicationError> handle(CreateUnitLotCommand command);

    Result<UnitLot, ApplicationError> handle(UpdateUnitLotCommand command);

    Result<Long, ApplicationError> handle(DeleteUnitLotCommand command);
}
