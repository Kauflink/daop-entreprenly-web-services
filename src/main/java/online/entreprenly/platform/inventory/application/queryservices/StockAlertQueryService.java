package online.entreprenly.platform.inventory.application.queryservices;

import online.entreprenly.platform.inventory.domain.model.entities.StockAlert;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllStockAlertsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetStockAlertByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for stock alert read queries.
 */
public interface StockAlertQueryService {
    /**
     * Handles retrieval of every currently raised stock alert.
     *
     * @param query get-all query
     * @return the derived stock alerts
     */
    List<StockAlert> handle(GetAllStockAlertsQuery query);

    /**
     * Handles retrieval of a single raised stock alert by its identifier.
     *
     * @param query stock-alert-id query
     * @return the matching stock alert, if present
     */
    Optional<StockAlert> handle(GetStockAlertByIdQuery query);
}
