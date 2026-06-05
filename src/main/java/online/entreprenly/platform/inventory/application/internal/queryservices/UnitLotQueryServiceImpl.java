package online.entreprenly.platform.inventory.application.internal.queryservices;

import online.entreprenly.platform.inventory.application.queryservices.UnitLotQueryService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetUnitLotByIdQuery;
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves unit lot read queries, scoped to the owner account.
 */
@Service
public class UnitLotQueryServiceImpl implements UnitLotQueryService {

    private final UnitLotRepository unitLotRepository;

    public UnitLotQueryServiceImpl(UnitLotRepository unitLotRepository) {
        this.unitLotRepository = unitLotRepository;
    }

    @Override
    public List<UnitLot> handle(GetAllUnitLotsQuery query) {
        return unitLotRepository.findAllByOwnerEmail(query.ownerEmail());
    }

    @Override
    public Optional<UnitLot> handle(GetUnitLotByIdQuery query) {
        return unitLotRepository.findByIdAndOwnerEmail(query.unitLotId(), query.ownerEmail());
    }
}
