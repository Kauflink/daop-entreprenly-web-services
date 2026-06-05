package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitLotCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateUnitLotResource;

/**
 * Assembler that translates {@link CreateUnitLotResource} into {@link CreateUnitLotCommand}.
 */
public class CreateUnitLotCommandFromResourceAssembler {

    public static CreateUnitLotCommand toCommandFromResource(String ownerEmail, CreateUnitLotResource resource) {
        return new CreateUnitLotCommand(
                ownerEmail,
                resource.productId(),
                resource.codeQR(),
                resource.entryDate(),
                resource.quantity(),
                resource.expiryDate());
    }
}
