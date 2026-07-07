package online.entreprenly.platform.inventory.application.acl;

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
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.WeightLotRepository;
import online.entreprenly.platform.inventory.interfaces.acl.CatalogItem;
import online.entreprenly.platform.inventory.interfaces.acl.InventoryContextFacade;
import online.entreprenly.platform.inventory.interfaces.acl.StockDeductionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Application-layer implementation of the Inventory ACL facade.
 *
 * <p>Computes catalog stock by summing lots and deducts confirmed-order quantities consuming
 * lots oldest-first (FIFO by entry date), exposing both to other bounded contexts without
 * coupling them to the Inventory internal model.</p>
 */
@Service
public class InventoryContextFacadeImpl implements InventoryContextFacade {

    private static final Logger log = LoggerFactory.getLogger(InventoryContextFacadeImpl.class);

    private final UnitProductQueryService unitProductQueryService;
    private final WeightProductQueryService weightProductQueryService;
    private final UnitLotQueryService unitLotQueryService;
    private final WeightLotQueryService weightLotQueryService;
    private final UnitLotRepository unitLotRepository;
    private final WeightLotRepository weightLotRepository;

    public InventoryContextFacadeImpl(UnitProductQueryService unitProductQueryService,
                                      WeightProductQueryService weightProductQueryService,
                                      UnitLotQueryService unitLotQueryService,
                                      WeightLotQueryService weightLotQueryService,
                                      UnitLotRepository unitLotRepository,
                                      WeightLotRepository weightLotRepository) {
        this.unitProductQueryService = unitProductQueryService;
        this.weightProductQueryService = weightProductQueryService;
        this.unitLotQueryService = unitLotQueryService;
        this.weightLotQueryService = weightLotQueryService;
        this.unitLotRepository = unitLotRepository;
        this.weightLotRepository = weightLotRepository;
    }

    @Override
    public List<CatalogItem> fetchCatalogByOwner(String ownerEmail) {
        if (ownerEmail == null || ownerEmail.isBlank()) {
            return List.of();
        }
        var catalog = new ArrayList<CatalogItem>();

        var unitLots = unitLotQueryService.handle(new GetAllUnitLotsQuery(ownerEmail));
        for (var product : unitProductQueryService.handle(new GetAllUnitProductsQuery(ownerEmail))) {
            double stock = unitLots.stream()
                    .filter(lot -> product.getId().equals(lot.getProductId()))
                    .mapToInt(UnitLot::getQuantity)
                    .sum();
            catalog.add(new CatalogItem(product.getName(), product.getPrice(), false, stock));
        }

        var weightLots = weightLotQueryService.handle(new GetAllWeightLotsQuery(ownerEmail));
        for (var product : weightProductQueryService.handle(new GetAllWeightProductsQuery(ownerEmail))) {
            double stock = weightLots.stream()
                    .filter(lot -> product.getId().equals(lot.getProductId()))
                    .mapToDouble(WeightLot::getQuantityKg)
                    .sum();
            catalog.add(new CatalogItem(product.getName(), product.getPricePerKg(), true, stock));
        }

        return catalog;
    }

    @Override
    public void decrementStockForItems(String ownerEmail, List<StockDeductionItem> items) {
        if (ownerEmail == null || ownerEmail.isBlank() || items == null || items.isEmpty()) {
            return;
        }
        var unitProducts = unitProductQueryService.handle(new GetAllUnitProductsQuery(ownerEmail));
        var weightProducts = weightProductQueryService.handle(new GetAllWeightProductsQuery(ownerEmail));

        for (var item : items) {
            if (item == null || item.productName() == null || item.quantity() <= 0) continue;
            var name = item.productName().trim();

            var unitProduct = unitProducts.stream()
                    .filter(p -> p.getName() != null && p.getName().trim().equalsIgnoreCase(name))
                    .findFirst();
            if (unitProduct.isPresent()) {
                deductFromUnitLots(ownerEmail, unitProduct.get().getId(), name, (int) Math.round(item.quantity()));
                continue;
            }

            var weightProduct = weightProducts.stream()
                    .filter(p -> p.getName() != null && p.getName().trim().equalsIgnoreCase(name))
                    .findFirst();
            if (weightProduct.isPresent()) {
                deductFromWeightLots(ownerEmail, weightProduct.get().getId(), name, item.quantity());
                continue;
            }

            log.warn("[inventory] confirmed order item '{}' (x{}) not found in {}'s catalog; skipping",
                    name, item.quantity(), ownerEmail);
        }
    }

    private void deductFromUnitLots(String ownerEmail, Long productId, String name, int quantity) {
        var lots = unitLotRepository.findAllByOwnerEmail(ownerEmail).stream()
                .filter(lot -> productId.equals(lot.getProductId()) && lot.getQuantity() > 0)
                .sorted(Comparator.comparing(UnitLot::getEntryDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        int remaining = quantity;
        for (var lot : lots) {
            if (remaining <= 0) break;
            remaining -= lot.consume(remaining);
            unitLotRepository.save(lot);
        }
        if (remaining > 0) {
            log.warn("[inventory] not enough stock for unit product '{}' ({}): {} unit(s) unfulfilled",
                    name, ownerEmail, remaining);
        }
    }

    private void deductFromWeightLots(String ownerEmail, Long productId, String name, double quantityKg) {
        var lots = weightLotRepository.findAllByOwnerEmail(ownerEmail).stream()
                .filter(lot -> productId.equals(lot.getProductId()) && lot.getQuantityKg() > 0)
                .sorted(Comparator.comparing(WeightLot::getEntryDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        double remaining = quantityKg;
        for (var lot : lots) {
            if (remaining <= 0) break;
            remaining -= lot.consume(remaining);
            weightLotRepository.save(lot);
        }
        if (remaining > 0.0001) {
            log.warn("[inventory] not enough stock for weight product '{}' ({}): {} kg unfulfilled",
                    name, ownerEmail, remaining);
        }
    }
}
