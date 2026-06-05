package online.entreprenly.platform.inventory.application.commandservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteWeightProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateWeightProductCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service contract for weight product commands.
 */
public interface WeightProductCommandService {
    Result<WeightProduct, ApplicationError> handle(CreateWeightProductCommand command);

    Result<WeightProduct, ApplicationError> handle(UpdateWeightProductCommand command);

    Result<Long, ApplicationError> handle(DeleteWeightProductCommand command);
}
