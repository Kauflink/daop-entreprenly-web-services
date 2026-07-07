package online.entreprenly.platform.sales.infrastructure.acl;

import online.entreprenly.platform.sales.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.sales.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.inventory.interfaces.acl.InventoryContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ACL adapter that fulfils the Sales product-catalog port using the Inventory bounded
 * context's published facade, translating its catalog items into the Sales-owned
 * {@link CatalogProduct}. This keeps the dependency on Inventory isolated to this adapter.
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
