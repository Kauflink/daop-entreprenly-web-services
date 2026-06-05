package online.entreprenly.platform.inventory.application.commandservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateUnitProductCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service contract for unit product commands.
 */
public interface UnitProductCommandService {
    /**
     * Registers a new unit product.
     *
     * @param command create command
     * @return created unit product, or an application error
     */
    Result<UnitProduct, ApplicationError> handle(CreateUnitProductCommand command);

    /**
     * Updates an existing unit product.
     *
     * @param command update command
     * @return updated unit product, or an application error
     */
    Result<UnitProduct, ApplicationError> handle(UpdateUnitProductCommand command);

    /**
     * Deletes a unit product.
     *
     * @param command delete command
     * @return the deleted product identifier, or an application error
     */
    Result<Long, ApplicationError> handle(DeleteUnitProductCommand command);
}
