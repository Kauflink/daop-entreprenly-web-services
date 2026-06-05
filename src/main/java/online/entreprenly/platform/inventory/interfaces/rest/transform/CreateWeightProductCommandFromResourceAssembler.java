package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightProductCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateWeightProductResource;

/**
 * Assembler that translates {@link CreateWeightProductResource} into {@link CreateWeightProductCommand}.
 */
public class CreateWeightProductCommandFromResourceAssembler {

    public static CreateWeightProductCommand toCommandFromResource(String ownerEmail,
                                                                   CreateWeightProductResource resource) {
        return new CreateWeightProductCommand(
                ownerEmail,
                resource.name(),
                resource.description(),
                resource.codeQR(),
                resource.pricePerKg());
    }
}
