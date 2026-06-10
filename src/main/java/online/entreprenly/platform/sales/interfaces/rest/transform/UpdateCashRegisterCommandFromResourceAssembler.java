package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.sales.domain.model.commands.UpdateCashRegisterCommand;
import online.entreprenly.platform.sales.interfaces.rest.resources.UpdateCashRegisterResource;

/**
 * Assembler that translates {@link UpdateCashRegisterResource} into {@link UpdateCashRegisterCommand}.
 */
public class UpdateCashRegisterCommandFromResourceAssembler {

    public static UpdateCashRegisterCommand toCommandFromResource(String ownerEmail, Long cashRegisterId,
                                                                  UpdateCashRegisterResource resource) {
        return new UpdateCashRegisterCommand(
                ownerEmail,
                cashRegisterId,
                resource.totalCash(),
                resource.totalDigital(),
                resource.saleCount());
    }
}
