package online.entreprenly.platform.inventory.application.internal.queryservices;

import online.entreprenly.platform.inventory.application.queryservices.WeightLotQueryService;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetWeightLotByIdQuery;
import online.entreprenly.platform.inventory.domain.repositories.WeightLotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves weight lot read queries, scoped to the owner account.
 */
@Service
public class WeightLotQueryServiceImpl implements WeightLotQueryService {

    private final WeightLotRepository weightLotRepository;

    public WeightLotQueryServiceImpl(WeightLotRepository weightLotRepository) {
        this.weightLotRepository = weightLotRepository;
    }

    @Override
    public List<WeightLot> handle(GetAllWeightLotsQuery query) {
        return weightLotRepository.findAllByOwnerEmail(query.ownerEmail());
    }

    @Override
    public Optional<WeightLot> handle(GetWeightLotByIdQuery query) {
        return weightLotRepository.findByIdAndOwnerEmail(query.weightLotId(), query.ownerEmail());
    }
}
