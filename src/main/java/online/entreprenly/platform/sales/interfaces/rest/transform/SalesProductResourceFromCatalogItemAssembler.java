package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.inventory.interfaces.acl.CatalogItem;
import online.entreprenly.platform.sales.interfaces.rest.resources.SalesProductResource;

/**
 * Assembler that translates an Inventory {@link CatalogItem} into a {@link SalesProductResource}
 * for the point-of-sale view.
 */
public class SalesProductResourceFromCatalogItemAssembler {

    public static SalesProductResource toResourceFromCatalogItem(CatalogItem item) {
        return new SalesProductResource(item.name(), item.price(), item.byWeight(), item.stock());
    }
}
