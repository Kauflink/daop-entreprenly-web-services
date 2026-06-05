package online.entreprenly.platform.inventory.application.queryservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetUnitProductByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for unit product read queries.
 */
public interface UnitProductQueryService {
    /**
     * Handles retrieval of every registered unit product.
     *
     * @param query get-all query
     * @return the registered unit products
     */
    List<UnitProduct> handle(GetAllUnitProductsQuery query);

    /**
     * Handles retrieval of a unit product by its identifier.
     *
     * @param query unit-product-id query
     * @return matching unit product, if found
     */
    Optional<UnitProduct> handle(GetUnitProductByIdQuery query);
}
