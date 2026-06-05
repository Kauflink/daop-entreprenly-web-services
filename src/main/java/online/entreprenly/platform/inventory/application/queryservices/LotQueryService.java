package online.entreprenly.platform.inventory.application.queryservices;

import online.entreprenly.platform.inventory.domain.model.entities.Lot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetLotByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for the combined lot read view.
 */
public interface LotQueryService {
    /**
     * Handles retrieval of every registered lot (unit and weight combined).
     *
     * @param query get-all query
     * @return the combined lots
     */
    List<Lot> handle(GetAllLotsQuery query);

    /**
     * Handles retrieval of a lot by its identifier from the combined view.
     *
     * @param query lot-id query
     * @return the matching lot, if present
     */
    Optional<Lot> handle(GetLotByIdQuery query);
}
