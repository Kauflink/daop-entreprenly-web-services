package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitProductCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateUnitProductResource;

/**
 * Assembler that translates {@link CreateUnitProductResource} into {@link CreateUnitProductCommand}.
 */
public class CreateUnitProductCommandFromResourceAssembler {

    public static CreateUnitProductCommand toCommandFromResource(String ownerEmail,
                                                                 CreateUnitProductResource resource) {
        return new CreateUnitProductCommand(
                ownerEmail,
                resource.name(),
                resource.description(),
                resource.codeQR(),
                resource.price(),
                resource.weightGrams(),
                resource.brand());
    }
}
