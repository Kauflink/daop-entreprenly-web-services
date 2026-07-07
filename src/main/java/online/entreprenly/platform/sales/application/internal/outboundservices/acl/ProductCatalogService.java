package online.entreprenly.platform.sales.application.internal.outboundservices.acl;

import online.entreprenly.platform.sales.domain.model.valueobjects.CatalogProduct;

import java.util.List;

/**
 * Outbound port through which the Sales context obtains the sellable product catalog
 * (products with computed stock) from another bounded context, without depending on its
 * internal model. Implemented in the infrastructure layer by an ACL adapter.
 */
public interface ProductCatalogService {

    List<CatalogProduct> findByOwner(String ownerEmail);
}
