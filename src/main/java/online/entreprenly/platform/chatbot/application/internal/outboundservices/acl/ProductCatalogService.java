package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;

import java.util.List;

/**
 * Outbound port (anti-corruption layer) that lets the chatbot read a seller's product
 * catalog from the Inventory bounded context.
 *
 * <p>The implementation translates inventory products and lots into {@link CatalogProduct}
 * snapshots, keeping the chatbot decoupled from the inventory domain model.</p>
 */
public interface ProductCatalogService {

    /**
     * Returns the catalog (products with price and computed stock) owned by a seller.
     *
     * @param ownerEmail the seller's account email that owns the products
     * @return the seller's catalog, or an empty list when there are no products
     */
    List<CatalogProduct> findByOwner(String ownerEmail);
}
