package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.InventoryStockService;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.inventory.application.queryservices.UnitProductQueryService;
import online.entreprenly.platform.inventory.application.queryservices.WeightProductQueryService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightProductsQuery;
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.WeightLotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * Anti-corruption layer adapter that deducts confirmed-order quantities from the
 * Inventory bounded context.
 *
 * <p>For each order line it locates the seller's product by name (unit first, then
 * weight) and consumes its lots oldest-first (FIFO by entry date), mirroring how
 * {@code InventoryProductCatalogService} computes available stock. Matching is by
 * trimmed, case-insensitive name because order lines capture the catalog name agreed
 * in the chat.</p>
 */
@Service
public class InventoryStockAdjuster implements InventoryStockService {

    private static final Logger log = LoggerFactory.getLogger(InventoryStockAdjuster.class);

    private final UnitProductQueryService unitProductQueryService;
    private final WeightProductQueryService weightProductQueryService;
    private final UnitLotRepository unitLotRepository;
    private final WeightLotRepository weightLotRepository;

    public InventoryStockAdjuster(UnitProductQueryService unitProductQueryService,
                                  WeightProductQueryService weightProductQueryService,
                                  UnitLotRepository unitLotRepository,
                                  WeightLotRepository weightLotRepository) {
        this.unitProductQueryService = unitProductQueryService;
        this.weightProductQueryService = weightProductQueryService;
        this.unitLotRepository = unitLotRepository;
        this.weightLotRepository = weightLotRepository;
    }

    @Override
    public void decrementForOrder(String ownerEmail, List<OrderItem> items) {
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
                deductFromUnitLots(ownerEmail, unitProduct.get().getId(), name, item.quantity());
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
