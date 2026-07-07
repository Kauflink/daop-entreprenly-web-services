package online.entreprenly.platform.sales.application.internal.commandservices;

import online.entreprenly.platform.sales.application.commandservices.SaleCommandService;
import online.entreprenly.platform.sales.application.internal.outboundservices.acl.StockDeductionService;
import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.sales.domain.repositories.SaleRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sale command service implementation.
 */
@Service
public class SaleCommandServiceImpl implements SaleCommandService {

    private static final Logger log = LoggerFactory.getLogger(SaleCommandServiceImpl.class);

    private final SaleRepository saleRepository;
    private final StockDeductionService stockDeductionService;

    public SaleCommandServiceImpl(SaleRepository saleRepository, StockDeductionService stockDeductionService) {
        this.saleRepository = saleRepository;
        this.stockDeductionService = stockDeductionService;
    }

    @Override
    public Result<Sale, ApplicationError> handle(CreateSaleCommand command) {
        if (command.ownerEmail() == null || command.ownerEmail().isBlank()) {
            return Result.failure(ApplicationError.validationError("owner", "An authenticated owner is required"));
        }
        if (command.sellerId() == null) {
            return Result.failure(ApplicationError.validationError("sellerId", "A seller is required to register a sale"));
        }
        if (command.items() == null || command.items().isEmpty()) {
            return Result.failure(ApplicationError.validationError("items", "A sale must contain at least one item"));
        }
        var sale = new Sale(command.ownerEmail(), command.sellerId(), command.items(), command.paymentMethod(),
                command.paymentReceipt(), command.status());
        var saved = saleRepository.save(sale);
        deductStock(command);
        return Result.success(saved);
    }

    /**
     * Deducts the sold quantities from inventory through the stock-deduction ACL port. A
     * failure here is logged but never rolls back the registered sale, so the point-of-sale
     * transaction stays recorded even if stock could not be adjusted.
     */
    private void deductStock(CreateSaleCommand command) {
        try {
            stockDeductionService.decrementForSale(command.ownerEmail(), command.items());
        } catch (Exception e) {
            log.warn("[sales] stock decrement failed for owner {}: {}", command.ownerEmail(), e.getMessage(), e);
        }
    }
}
