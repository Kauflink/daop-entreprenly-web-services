package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.inventory.application.queryservices.UnitLotQueryService;
import online.entreprenly.platform.inventory.application.queryservices.UnitProductQueryService;
import online.entreprenly.platform.inventory.application.queryservices.WeightLotQueryService;
import online.entreprenly.platform.inventory.application.queryservices.WeightProductQueryService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightProductsQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Anti-corruption layer adapter that builds the chatbot's {@link CatalogProduct} view
 * from the Inventory bounded context.
 *
 * <p>It reads the seller's unit and weight products and computes the available stock by
 * summing their lots, translating both into a single catalog the chatbot can reason over.</p>
 */
@Service
public class InventoryProductCatalogService implements ProductCatalogService {

    private final UnitProductQueryService unitProductQueryService;
    private final WeightProductQueryService weightProductQueryService;
    private final UnitLotQueryService unitLotQueryService;
    private final WeightLotQueryService weightLotQueryService;

    public InventoryProductCatalogService(UnitProductQueryService unitProductQueryService,
                                          WeightProductQueryService weightProductQueryService,
                                          UnitLotQueryService unitLotQueryService,
                                          WeightLotQueryService weightLotQueryService) {
        this.unitProductQueryService = unitProductQueryService;
        this.weightProductQueryService = weightProductQueryService;
        this.unitLotQueryService = unitLotQueryService;
        this.weightLotQueryService = weightLotQueryService;
    }

    @Override
    public List<CatalogProduct> findByOwner(String ownerEmail) {
        if (ownerEmail == null || ownerEmail.isBlank()) {
            return List.of();
        }
        var catalog = new ArrayList<CatalogProduct>();

        var unitLots = unitLotQueryService.handle(new GetAllUnitLotsQuery(ownerEmail));
        for (var product : unitProductQueryService.handle(new GetAllUnitProductsQuery(ownerEmail))) {
            double stock = unitLots.stream()
                    .filter(lot -> product.getId().equals(lot.getProductId()))
                    .mapToInt(UnitLot::getQuantity)
                    .sum();
            catalog.add(new CatalogProduct(product.getName(), product.getPrice(), false, stock));
        }

        var weightLots = weightLotQueryService.handle(new GetAllWeightLotsQuery(ownerEmail));
        for (var product : weightProductQueryService.handle(new GetAllWeightProductsQuery(ownerEmail))) {
            double stock = weightLots.stream()
                    .filter(lot -> product.getId().equals(lot.getProductId()))
                    .mapToDouble(WeightLot::getQuantityKg)
                    .sum();
            catalog.add(new CatalogProduct(product.getName(), product.getPricePerKg(), true, stock));
        }

        return catalog;
    }
}
