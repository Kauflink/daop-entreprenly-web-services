package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.entities.Lot;
import online.entreprenly.platform.inventory.interfaces.rest.resources.LotResource;

/**
 * Assembler that converts {@link Lot} read models into {@link LotResource} objects.
 */
public class LotResourceFromEntityAssembler {

    public static LotResource toResourceFromEntity(Lot lot) {
        return new LotResource(
                lot.getId(),
                lot.getProductId(),
                lot.getCodeQR(),
                lot.getEntryDate(),
                lot.getLotType().getValue());
    }
}
