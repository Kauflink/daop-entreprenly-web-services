package online.entreprenly.platform.sales.infrastructure.acl;

import online.entreprenly.platform.sales.application.internal.outboundservices.acl.StockDeductionService;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.inventory.interfaces.acl.InventoryContextFacade;
import online.entreprenly.platform.inventory.interfaces.acl.StockDeductionItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ACL adapter that fulfils the Sales stock-deduction port using the Inventory bounded
 * context's published facade, translating sale line items into the Inventory
 * {@link StockDeductionItem}. Keeps the dependency on Inventory isolated to this adapter.
 */
@Service
public class InventoryStockDeductionService implements StockDeductionService {

    private final InventoryContextFacade inventoryContextFacade;

    public InventoryStockDeductionService(InventoryContextFacade inventoryContextFacade) {
        this.inventoryContextFacade = inventoryContextFacade;
    }

    @Override
    public void decrementForSale(String ownerEmail, List<SaleItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        var deductions = items.stream()
                .map(item -> new StockDeductionItem(item.productName(), quantityToDeduct(item)))
                .toList();
        inventoryContextFacade.decrementStockForItems(ownerEmail, deductions);
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
