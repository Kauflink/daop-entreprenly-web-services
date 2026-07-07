package online.entreprenly.platform.sales.application.internal.commandservices;

import online.entreprenly.platform.sales.application.commandservices.SaleCommandService;
import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.sales.domain.repositories.SaleRepository;
import online.entreprenly.platform.inventory.interfaces.acl.InventoryContextFacade;
import online.entreprenly.platform.inventory.interfaces.acl.StockDeductionItem;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Sale command service implementation.
 */
@Service
public class SaleCommandServiceImpl implements SaleCommandService {

    private static final Logger log = LoggerFactory.getLogger(SaleCommandServiceImpl.class);

    private final SaleRepository saleRepository;
    private final InventoryContextFacade inventoryContextFacade;

    public SaleCommandServiceImpl(SaleRepository saleRepository, InventoryContextFacade inventoryContextFacade) {
        this.saleRepository = saleRepository;
        this.inventoryContextFacade = inventoryContextFacade;
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
     * Deducts the sold quantities from inventory through the Inventory ACL facade, consuming
     * lots oldest-first. A failure here is logged but never rolls back the registered sale, so
     * the point-of-sale transaction stays recorded even if stock could not be adjusted.
     */
    private void deductStock(CreateSaleCommand command) {
        try {
            List<StockDeductionItem> deductions = command.items().stream()
                    .map(item -> new StockDeductionItem(item.productName(), quantityToDeduct(item)))
                    .toList();
            inventoryContextFacade.decrementStockForItems(command.ownerEmail(), deductions);
        } catch (Exception e) {
            log.warn("[sales] stock decrement failed for owner {}: {}", command.ownerEmail(), e.getMessage(), e);
        }
    }

    private static double quantityToDeduct(SaleItem item) {
        if (item.quantity() != null) {
            return item.quantity();
        }
        if (item.weightKg() != null) {
            return item.weightKg();
        }
        return 0d;
    }
}
