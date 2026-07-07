package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.sales.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.sales.interfaces.rest.resources.SalesProductResource;

/**
 * Assembler that translates a {@link CatalogProduct} into a {@link SalesProductResource}
 * for the point-of-sale view.
 */
public class SalesProductResourceFromCatalogProductAssembler {

    public static SalesProductResource toResourceFromCatalogProduct(CatalogProduct product) {
        return new SalesProductResource(product.name(), product.price(), product.byWeight(), product.stock());
    }
}
