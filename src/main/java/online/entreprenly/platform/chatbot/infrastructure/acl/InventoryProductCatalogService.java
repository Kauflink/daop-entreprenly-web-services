package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.inventory.interfaces.acl.InventoryContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Anti-corruption layer adapter that builds the chatbot's {@link CatalogProduct} view from
 * the Inventory bounded context through its ACL facade, keeping the chatbot decoupled from
 * the inventory domain model.
 */
@Service
public class InventoryProductCatalogService implements ProductCatalogService {

    private final InventoryContextFacade inventoryContextFacade;

    public InventoryProductCatalogService(InventoryContextFacade inventoryContextFacade) {
        this.inventoryContextFacade = inventoryContextFacade;
    }

    @Override
    public List<CatalogProduct> findByOwner(String ownerEmail) {
        return inventoryContextFacade.fetchCatalogByOwner(ownerEmail).stream()
                .map(item -> new CatalogProduct(item.name(), item.price(), item.byWeight(), item.stock()))
                .toList();
    }
}
