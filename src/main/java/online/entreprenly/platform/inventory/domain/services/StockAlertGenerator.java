package online.entreprenly.platform.inventory.domain.services;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.domain.model.entities.StockAlert;
import online.entreprenly.platform.inventory.domain.model.valueobjects.AlertSeverity;
import online.entreprenly.platform.inventory.domain.model.valueobjects.AlertType;
import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Domain service that derives {@link StockAlert} read models from the current state of
 * inventory products and their lots.
 *
 * <p>The rules mirror the storefront expectations: out-of-stock and low-stock thresholds,
 * soon-to-expire windows and already-expired lots. Alerts are returned ordered from most
 * to least urgent and carry stable sequential identifiers within a generation run.</p>
 */
public final class StockAlertGenerator {

    private static final int EXPIRING_SOON_DAYS = 5;
    private static final int LOW_UNIT_STOCK_THRESHOLD = 5;
    private static final double LOW_WEIGHT_STOCK_THRESHOLD = 5d;

    private StockAlertGenerator() {
    }

    /**
     * Generates the stock alerts implied by the supplied inventory snapshot.
     *
     * @param unitProducts   the registered unit products
     * @param weightProducts the registered weight products
     * @param unitLots       the registered unit lots
     * @param weightLots     the registered weight lots
     * @param now            the reference instant used for expiry calculations
     * @return the derived alerts ordered by descending urgency, with sequential ids
     */
    public static List<StockAlert> generate(List<UnitProduct> unitProducts, List<WeightProduct> weightProducts,
                                            List<UnitLot> unitLots, List<WeightLot> weightLots, Instant now) {
        List<StockAlert> alerts = new ArrayList<>();
        LocalDate today = LocalDate.ofInstant(now, ZoneOffset.UTC);

        for (UnitProduct product : unitProducts) {
            List<UnitLot> productLots = unitLots.stream()
                    .filter(l -> product.getId().equals(l.getProductId()))
                    .toList();
            int totalStock = productLots.stream().mapToInt(UnitLot::getQuantity).sum();
            List<UnitLot> outLots = productLots.stream().filter(l -> l.getQuantity() <= 0).toList();

            for (UnitLot lot : outLots) {
                alerts.add(makeAlert(AlertType.OUT_OF_STOCK, product.getId(), product.getName(),
                        ProductType.UNIT, lot.getId(), now));
            }
            if (outLots.isEmpty() && totalStock <= 0) {
                alerts.add(makeAlert(AlertType.OUT_OF_STOCK, product.getId(), product.getName(),
                        ProductType.UNIT, null, now));
            }
            if (totalStock > 0 && totalStock <= LOW_UNIT_STOCK_THRESHOLD) {
                Long lotId = productLots.isEmpty() ? null : productLots.get(0).getId();
                alerts.add(makeAlert(AlertType.LOW_STOCK, product.getId(), product.getName(),
                        ProductType.UNIT, lotId, now));
            }
            for (UnitLot lot : productLots) {
                if (lot.getExpiryDate() == null) continue;
                LocalDate expiry = LocalDate.ofInstant(lot.getExpiryDate(), ZoneOffset.UTC);
                long days = ChronoUnit.DAYS.between(today, expiry);
                if (days < 0) {
                    alerts.add(makeAlert(AlertType.EXPIRED, product.getId(), product.getName(),
                            ProductType.UNIT, lot.getId(), now));
                } else if (days <= EXPIRING_SOON_DAYS) {
                    alerts.add(makeAlert(AlertType.EXPIRING_SOON, product.getId(), product.getName(),
                            ProductType.UNIT, lot.getId(), now));
                }
            }
        }

        for (WeightProduct product : weightProducts) {
            List<WeightLot> productLots = weightLots.stream()
                    .filter(l -> product.getId().equals(l.getProductId()))
                    .toList();
            double totalStock = productLots.stream().mapToDouble(WeightLot::getQuantityKg).sum();
            List<WeightLot> outLots = productLots.stream().filter(l -> l.getQuantityKg() <= 0).toList();

            for (WeightLot lot : outLots) {
                alerts.add(makeAlert(AlertType.OUT_OF_STOCK, product.getId(), product.getName(),
                        ProductType.WEIGHT, lot.getId(), now));
            }
            if (outLots.isEmpty() && totalStock <= 0) {
                alerts.add(makeAlert(AlertType.OUT_OF_STOCK, product.getId(), product.getName(),
                        ProductType.WEIGHT, null, now));
            }
            if (totalStock > 0 && totalStock <= LOW_WEIGHT_STOCK_THRESHOLD) {
                Long lotId = productLots.isEmpty() ? null : productLots.get(0).getId();
                alerts.add(makeAlert(AlertType.LOW_STOCK, product.getId(), product.getName(),
                        ProductType.WEIGHT, lotId, now));
            }
        }

        alerts.sort(Comparator.comparingInt(a -> a.getAlertType().getPriority()));

        AtomicLong sequence = new AtomicLong(1);
        return alerts.stream()
                .map(alert -> alert.withId(sequence.getAndIncrement()))
                .toList();
    }

    private static StockAlert makeAlert(AlertType alertType, Long productId, String productName,
                                        ProductType productType, Long lotId, Instant now) {
        AlertSeverity severity = (alertType == AlertType.EXPIRED || alertType == AlertType.OUT_OF_STOCK)
                ? AlertSeverity.CRITICAL
                : AlertSeverity.WARNING;
        return new StockAlert(0L, lotId, productId, productType, productName, alertType, severity,
                buildMessage(alertType, productName), now);
    }

    private static String buildMessage(AlertType alertType, String productName) {
        String name = productName == null ? "Product" : productName;
        return switch (alertType) {
            case EXPIRED -> "%s has an expired lot".formatted(name);
            case OUT_OF_STOCK -> "%s is out of stock".formatted(name);
            case EXPIRING_SOON -> "%s has a lot expiring soon".formatted(name);
            case LOW_STOCK -> "%s is running low on stock".formatted(name);
        };
    }
}
