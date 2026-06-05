package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.UpdateUnitProductCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateUnitProductResource;

/**
 * Assembler that translates {@link UpdateUnitProductResource} into {@link UpdateUnitProductCommand}.
 */
public class UpdateUnitProductCommandFromResourceAssembler {

    public static UpdateUnitProductCommand toCommandFromResource(Long unitProductId,
                                                                 UpdateUnitProductResource resource) {
        return new UpdateUnitProductCommand(
                unitProductId,
                resource.name(),
                resource.description(),
                resource.codeQR(),
                resource.price(),
                resource.weightGrams(),
                resource.brand());
    }
}
