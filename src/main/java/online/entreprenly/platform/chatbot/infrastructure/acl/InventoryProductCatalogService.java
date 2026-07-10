package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.inventory.interfaces.acl.InventoryContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("chatbotInventoryProductCatalogService")
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
