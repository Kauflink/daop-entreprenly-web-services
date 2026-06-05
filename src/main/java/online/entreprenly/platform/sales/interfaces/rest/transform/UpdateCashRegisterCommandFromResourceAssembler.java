package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.sales.domain.model.commands.UpdateCashRegisterCommand;
import online.entreprenly.platform.sales.interfaces.rest.resources.UpdateCashRegisterResource;

/**
 * Assembler that translates {@link UpdateCashRegisterResource} into {@link UpdateCashRegisterCommand}.
 */
public class UpdateCashRegisterCommandFromResourceAssembler {

    public static UpdateCashRegisterCommand toCommandFromResource(Long cashRegisterId, UpdateCashRegisterResource resource) {
        return new UpdateCashRegisterCommand(
                cashRegisterId,
                resource.totalCash(),
                resource.totalDigital(),
                resource.saleCount());
    }
}
