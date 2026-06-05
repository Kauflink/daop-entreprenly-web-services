package online.entreprenly.platform.inventory.application.internal.queryservices;

import online.entreprenly.platform.inventory.application.queryservices.UnitProductQueryService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetUnitProductByIdQuery;
import online.entreprenly.platform.inventory.domain.repositories.UnitProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves unit product read queries, scoped to the owner account.
 */
@Service
public class UnitProductQueryServiceImpl implements UnitProductQueryService {

    private final UnitProductRepository unitProductRepository;

    public UnitProductQueryServiceImpl(UnitProductRepository unitProductRepository) {
        this.unitProductRepository = unitProductRepository;
    }

    @Override
    public List<UnitProduct> handle(GetAllUnitProductsQuery query) {
        return unitProductRepository.findAllByOwnerEmail(query.ownerEmail());
    }

    @Override
    public Optional<UnitProduct> handle(GetUnitProductByIdQuery query) {
        return unitProductRepository.findByIdAndOwnerEmail(query.unitProductId(), query.ownerEmail());
    }
}
