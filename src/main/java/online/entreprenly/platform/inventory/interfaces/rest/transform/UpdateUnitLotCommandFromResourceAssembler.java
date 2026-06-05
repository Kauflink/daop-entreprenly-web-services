package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.UpdateUnitLotCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateUnitLotResource;

/**
 * Assembler that translates {@link UpdateUnitLotResource} into {@link UpdateUnitLotCommand}.
 */
public class UpdateUnitLotCommandFromResourceAssembler {

    public static UpdateUnitLotCommand toCommandFromResource(String ownerEmail, Long unitLotId,
                                                             UpdateUnitLotResource resource) {
        return new UpdateUnitLotCommand(
                ownerEmail,
                unitLotId,
                resource.productId(),
                resource.codeQR(),
                resource.entryDate(),
                resource.quantity(),
                resource.expiryDate());
    }
}
