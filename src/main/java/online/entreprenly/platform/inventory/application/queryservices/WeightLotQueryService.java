package online.entreprenly.platform.inventory.application.queryservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetWeightLotByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for weight lot read queries.
 */
public interface WeightLotQueryService {
    List<WeightLot> handle(GetAllWeightLotsQuery query);

    Optional<WeightLot> handle(GetWeightLotByIdQuery query);
}
