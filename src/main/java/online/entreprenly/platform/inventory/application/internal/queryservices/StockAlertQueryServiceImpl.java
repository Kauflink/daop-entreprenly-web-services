package online.entreprenly.platform.inventory.application.internal.queryservices;

import online.entreprenly.platform.inventory.application.queryservices.StockAlertQueryService;
import online.entreprenly.platform.inventory.domain.model.entities.StockAlert;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllStockAlertsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetStockAlertByIdQuery;
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.UnitProductRepository;
import online.entreprenly.platform.inventory.domain.repositories.WeightLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.WeightProductRepository;
import online.entreprenly.platform.inventory.domain.services.StockAlertGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Application service that derives stock alerts from the current inventory snapshot of an account.
 */
@Service
public class StockAlertQueryServiceImpl implements StockAlertQueryService {

    private final UnitProductRepository unitProductRepository;
    private final WeightProductRepository weightProductRepository;
    private final UnitLotRepository unitLotRepository;
    private final WeightLotRepository weightLotRepository;

    public StockAlertQueryServiceImpl(UnitProductRepository unitProductRepository,
                                      WeightProductRepository weightProductRepository,
                                      UnitLotRepository unitLotRepository,
                                      WeightLotRepository weightLotRepository) {
        this.unitProductRepository = unitProductRepository;
        this.weightProductRepository = weightProductRepository;
        this.unitLotRepository = unitLotRepository;
        this.weightLotRepository = weightLotRepository;
    }

    @Override
    public List<StockAlert> handle(GetAllStockAlertsQuery query) {
        var ownerEmail = query.ownerEmail();
        return StockAlertGenerator.generate(
                unitProductRepository.findAllByOwnerEmail(ownerEmail),
                weightProductRepository.findAllByOwnerEmail(ownerEmail),
                unitLotRepository.findAllByOwnerEmail(ownerEmail),
                weightLotRepository.findAllByOwnerEmail(ownerEmail),
                Instant.now());
    }

    @Override
    public Optional<StockAlert> handle(GetStockAlertByIdQuery query) {
        return handle(new GetAllStockAlertsQuery(query.ownerEmail())).stream()
                .filter(alert -> alert.getId().equals(query.stockAlertId()))
                .findFirst();
    }
}
