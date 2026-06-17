package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.InventoryStockService;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.inventory.interfaces.acl.InventoryContextFacade;
import online.entreprenly.platform.inventory.interfaces.acl.StockDeductionItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class InventoryStockAdjuster implements InventoryStockService {

    private final InventoryContextFacade inventoryContextFacade;

    public InventoryStockAdjuster(InventoryContextFacade inventoryContextFacade) {
        this.inventoryContextFacade = inventoryContextFacade;
    }

    @Override
    public void decrementForOrder(String ownerEmail, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        var deductions = items.stream()
                .filter(Objects::nonNull)
                .map(item -> new StockDeductionItem(item.productName(), item.quantity()))
                .toList();
        inventoryContextFacade.decrementStockForItems(ownerEmail, deductions);
    }
}
