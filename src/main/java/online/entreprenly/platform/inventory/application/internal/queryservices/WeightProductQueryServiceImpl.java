package online.entreprenly.platform.inventory.application.internal.queryservices;

import online.entreprenly.platform.inventory.application.queryservices.WeightProductQueryService;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetWeightProductByIdQuery;
import online.entreprenly.platform.inventory.domain.repositories.WeightProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves weight product read queries, scoped to the owner account.
 */
@Service
public class WeightProductQueryServiceImpl implements WeightProductQueryService {

    private final WeightProductRepository weightProductRepository;

    public WeightProductQueryServiceImpl(WeightProductRepository weightProductRepository) {
        this.weightProductRepository = weightProductRepository;
    }

    @Override
    public List<WeightProduct> handle(GetAllWeightProductsQuery query) {
        return weightProductRepository.findAllByOwnerEmail(query.ownerEmail());
    }

    @Override
    public Optional<WeightProduct> handle(GetWeightProductByIdQuery query) {
        return weightProductRepository.findByIdAndOwnerEmail(query.weightProductId(), query.ownerEmail());
    }
}
