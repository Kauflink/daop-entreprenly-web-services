package online.entreprenly.platform.inventory.domain.model.entities;

import online.entreprenly.platform.inventory.domain.model.valueobjects.AlertSeverity;
import online.entreprenly.platform.inventory.domain.model.valueobjects.AlertType;
import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import lombok.Getter;

import java.time.Instant;

/**
 * Stock alert read model.
 *
 * <p>A stock alert is a derived (computed) view raised from the current state of
 * inventory products and their lots; it is never persisted. It signals low stock,
 * out-of-stock, soon-to-expire and expired situations so the storefront can react.</p>
 */
@Getter
public class StockAlert {

    private final Long id;
    private final Long lotId;
    private final Long productId;
    private final ProductType productType;
    private final String productName;
    private final AlertType alertType;
    private final AlertSeverity severity;
    private final String message;
    private final Instant createdAt;

    public StockAlert(Long id, Long lotId, Long productId, ProductType productType, String productName,
                      AlertType alertType, AlertSeverity severity, String message, Instant createdAt) {
        this.id = id;
        this.lotId = lotId;
        this.productId = productId;
        this.productType = productType;
        this.productName = productName;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.createdAt = createdAt;
    }

    /**
     * Returns a copy of this alert with the given identifier assigned.
     *
     * @param newId the identifier to assign
     * @return a new stock alert carrying the identifier
     */
    public StockAlert withId(Long newId) {
        return new StockAlert(newId, lotId, productId, productType, productName, alertType, severity, message, createdAt);
    }
}
