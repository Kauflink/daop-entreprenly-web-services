package online.entreprenly.platform.inventory.application.internal.queryservices;

import online.entreprenly.platform.inventory.application.queryservices.LotQueryService;
import online.entreprenly.platform.inventory.domain.model.entities.Lot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetLotByIdQuery;
import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.WeightLotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Application service that assembles the combined lot read view from the unit and
 * weight lot aggregates owned by an account.
 */
@Service
public class LotQueryServiceImpl implements LotQueryService {

    private final UnitLotRepository unitLotRepository;
    private final WeightLotRepository weightLotRepository;

    public LotQueryServiceImpl(UnitLotRepository unitLotRepository, WeightLotRepository weightLotRepository) {
        this.unitLotRepository = unitLotRepository;
        this.weightLotRepository = weightLotRepository;
    }

    @Override
    public List<Lot> handle(GetAllLotsQuery query) {
        var ownerEmail = query.ownerEmail();
        List<Lot> lots = new ArrayList<>();
        unitLotRepository.findAllByOwnerEmail(ownerEmail).forEach(lot -> lots.add(new Lot(
                lot.getId(), lot.getProductId(), lot.getCodeQR(), lot.getEntryDate(), ProductType.UNIT)));
        weightLotRepository.findAllByOwnerEmail(ownerEmail).forEach(lot -> lots.add(new Lot(
                lot.getId(), lot.getProductId(), lot.getCodeQR(), lot.getEntryDate(), ProductType.WEIGHT)));
        return lots;
    }

    @Override
    public Optional<Lot> handle(GetLotByIdQuery query) {
        return handle(new GetAllLotsQuery(query.ownerEmail())).stream()
                .filter(lot -> lot.getId().equals(query.lotId()))
                .findFirst();
    }
}
