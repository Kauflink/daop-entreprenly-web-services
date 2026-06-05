package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UnitLotResource;

/**
 * Assembler that converts {@link UnitLot} aggregates into {@link UnitLotResource} objects.
 */
public class UnitLotResourceFromEntityAssembler {

    public static UnitLotResource toResourceFromEntity(UnitLot unitLot) {
        return new UnitLotResource(
                unitLot.getId(),
                unitLot.getProductId(),
                unitLot.getCodeQR(),
                unitLot.getEntryDate(),
                unitLot.getLotType().getValue(),
                unitLot.getQuantity(),
                unitLot.getExpiryDate());
    }
}
