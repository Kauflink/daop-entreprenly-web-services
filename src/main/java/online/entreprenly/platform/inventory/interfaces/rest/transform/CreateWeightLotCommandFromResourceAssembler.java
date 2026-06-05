package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightLotCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateWeightLotResource;

/**
 * Assembler that translates {@link CreateWeightLotResource} into {@link CreateWeightLotCommand}.
 */
public class CreateWeightLotCommandFromResourceAssembler {

    public static CreateWeightLotCommand toCommandFromResource(CreateWeightLotResource resource) {
        return new CreateWeightLotCommand(
                resource.productId(),
                resource.codeQR(),
                resource.entryDate(),
                resource.quantityKg());
    }
}
