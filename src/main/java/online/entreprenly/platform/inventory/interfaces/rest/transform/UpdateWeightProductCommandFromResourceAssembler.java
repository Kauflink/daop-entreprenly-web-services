package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.commands.UpdateWeightProductCommand;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateWeightProductResource;

/**
 * Assembler that translates {@link UpdateWeightProductResource} into {@link UpdateWeightProductCommand}.
 */
public class UpdateWeightProductCommandFromResourceAssembler {

    public static UpdateWeightProductCommand toCommandFromResource(String ownerEmail, Long weightProductId,
                                                                   UpdateWeightProductResource resource) {
        return new UpdateWeightProductCommand(
                ownerEmail,
                weightProductId,
                resource.name(),
                resource.description(),
                resource.codeQR(),
                resource.pricePerKg());
    }
}
