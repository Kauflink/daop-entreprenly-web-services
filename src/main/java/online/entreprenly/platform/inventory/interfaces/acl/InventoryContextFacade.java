package online.entreprenly.platform.inventory.interfaces.acl;

import java.util.List;

/**
 * ACL facade that exposes Inventory bounded context capabilities to other contexts.
 *
 * <p>Provides a simplified integration surface for reading a seller's catalog with computed
 * stock and for deducting confirmed-order quantities, without leaking the Inventory internal
 * model.</p>
 */
public interface InventoryContextFacade {
    /**
     * Returns the catalog (products with price and computed stock) owned by a seller.
     *
     * @param ownerEmail the seller's account email that owns the products
     * @return the seller's catalog items, or an empty list when there are none
     */
    List<CatalogItem> fetchCatalogByOwner(String ownerEmail);

    /**
     * Deducts the given quantities from the seller's stock, consuming lots oldest-first.
     *
     * @param ownerEmail the seller's account email that owns the products
     * @param items      the products and quantities to deduct, matched by trimmed name
     */
    void decrementStockForItems(String ownerEmail, List<StockDeductionItem> items);
}
