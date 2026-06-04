package online.entreprenly.platform.sales.application.internal.commandservices;

import online.entreprenly.platform.sales.application.commandservices.SaleCommandService;
import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.sales.domain.repositories.SaleRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Sale command service implementation.
 */
@Service
public class SaleCommandServiceImpl implements SaleCommandService {

    private final SaleRepository saleRepository;

    public SaleCommandServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Result<Sale, ApplicationError> handle(CreateSaleCommand command) {
        if (command.sellerId() == null) {
            return Result.failure(ApplicationError.validationError("sellerId", "A seller is required to register a sale"));
        }
        if (command.items() == null || command.items().isEmpty()) {
            return Result.failure(ApplicationError.validationError("items", "A sale must contain at least one item"));
        }
        var sale = new Sale(command.sellerId(), command.items(), command.paymentMethod(),
                command.paymentReceipt(), command.status());
        return Result.success(saleRepository.save(sale));
    }
}
