package online.entreprenly.platform.inventory.application.queryservices;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetWeightProductByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for weight product read queries.
 */
public interface WeightProductQueryService {
    List<WeightProduct> handle(GetAllWeightProductsQuery query);

    Optional<WeightProduct> handle(GetWeightProductByIdQuery query);
}
