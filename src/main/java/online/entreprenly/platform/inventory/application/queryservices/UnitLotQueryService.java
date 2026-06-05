package online.entreprenly.platform.inventory.application.queryservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetUnitLotByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for unit lot read queries.
 */
public interface UnitLotQueryService {
    List<UnitLot> handle(GetAllUnitLotsQuery query);

    Optional<UnitLot> handle(GetUnitLotByIdQuery query);
}
