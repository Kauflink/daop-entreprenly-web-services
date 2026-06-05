package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.entities.StockAlert;
import online.entreprenly.platform.inventory.interfaces.rest.resources.StockAlertResource;

/**
 * Assembler that converts {@link StockAlert} read models into {@link StockAlertResource} objects.
 */
public class StockAlertResourceFromEntityAssembler {

    public static StockAlertResource toResourceFromEntity(StockAlert stockAlert) {
        return new StockAlertResource(
                stockAlert.getId(),
                stockAlert.getLotId(),
                stockAlert.getProductId(),
                stockAlert.getProductType() == null ? null : stockAlert.getProductType().getValue(),
                stockAlert.getProductName(),
                stockAlert.getAlertType().getValue(),
                stockAlert.getSeverity().getValue(),
                stockAlert.getMessage(),
                stockAlert.getCreatedAt());
    }
}
