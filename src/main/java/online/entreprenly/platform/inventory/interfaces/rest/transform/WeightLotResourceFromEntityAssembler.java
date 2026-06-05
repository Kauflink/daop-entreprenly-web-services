package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.interfaces.rest.resources.WeightLotResource;

/**
 * Assembler that converts {@link WeightLot} aggregates into {@link WeightLotResource} objects.
 */
public class WeightLotResourceFromEntityAssembler {

    public static WeightLotResource toResourceFromEntity(WeightLot weightLot) {
        return new WeightLotResource(
                weightLot.getId(),
                weightLot.getProductId(),
                weightLot.getCodeQR(),
                weightLot.getEntryDate(),
                weightLot.getLotType().getValue(),
                weightLot.getQuantityKg());
    }
}
