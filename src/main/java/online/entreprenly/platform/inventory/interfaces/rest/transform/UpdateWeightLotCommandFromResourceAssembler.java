package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.UpdateWeightLotCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateWeightLotResource;

/**
 * Assembler that translates {@link UpdateWeightLotResource} into {@link UpdateWeightLotCommand}.
 */
public class UpdateWeightLotCommandFromResourceAssembler {

    public static UpdateWeightLotCommand toCommandFromResource(String ownerEmail, Long weightLotId,
                                                               UpdateWeightLotResource resource) {
        return new UpdateWeightLotCommand(
                ownerEmail,
                weightLotId,
                resource.productId(),
                resource.codeQR(),
                resource.entryDate(),
                resource.quantityKg());
    }
}
