package online.entreprenly.platform.sales.application.queryservices;

import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.queries.GetAllSalesQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetSaleByIdQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetSalesByDateQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for sale read queries.
 */
public interface SaleQueryService {
    /**
     * Handles retrieval of every registered sale.
     *
     * @param query get-all query
     * @return the registered sales
     */
    List<Sale> handle(GetAllSalesQuery query);

    /**
     * Handles retrieval of the sales registered on a given business day.
     *
     * @param query get-by-date query
     * @return the sales registered on that day
     */
    List<Sale> handle(GetSalesByDateQuery query);

    /**
     * Handles retrieval of a sale by its identifier.
     *
     * @param query sale-id query
     * @return matching sale, if found
     */
    Optional<Sale> handle(GetSaleByIdQuery query);
}
