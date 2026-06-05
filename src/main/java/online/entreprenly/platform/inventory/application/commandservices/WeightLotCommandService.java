package online.entreprenly.platform.inventory.application.commandservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteWeightLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateWeightLotCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service contract for weight lot commands.
 */
public interface WeightLotCommandService {
    Result<WeightLot, ApplicationError> handle(CreateWeightLotCommand command);

    Result<WeightLot, ApplicationError> handle(UpdateWeightLotCommand command);

    Result<Long, ApplicationError> handle(DeleteWeightLotCommand command);
}
