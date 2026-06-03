package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.sales.domain.model.commands.CreateCashRegisterCommand;
import online.entreprenly.platform.sales.interfaces.rest.resources.CreateCashRegisterResource;

/**
 * Assembler that translates {@link CreateCashRegisterResource} into {@link CreateCashRegisterCommand}.
 */
public class CreateCashRegisterCommandFromResourceAssembler {

    public static CreateCashRegisterCommand toCommandFromResource(CreateCashRegisterResource resource) {
        return new CreateCashRegisterCommand(
                resource.date(),
                resource.totalCash(),
                resource.totalDigital());
    }
}
