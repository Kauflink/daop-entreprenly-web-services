package online.entreprenly.platform.sales.application.commandservices;

import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service contract for sale commands.
 */
public interface SaleCommandService {
    /**
     * Registers a new sale.
     *
     * @param command create command
     * @return created sale, or an application error
     */
    Result<Sale, ApplicationError> handle(CreateSaleCommand command);
}
