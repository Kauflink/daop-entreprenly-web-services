package online.entreprenly.platform.sales.interfaces.rest.transform;

import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;
import online.entreprenly.platform.sales.interfaces.rest.resources.CashRegisterResource;

/**
 * Assembler that converts {@link CashRegister} aggregates into {@link CashRegisterResource} objects.
 */
public class CashRegisterResourceFromEntityAssembler {

    public static CashRegisterResource toResourceFromEntity(CashRegister cashRegister) {
        return new CashRegisterResource(
                cashRegister.getId(),
                cashRegister.getDate(),
                cashRegister.getTotalCash(),
                cashRegister.getTotalDigital(),
                cashRegister.getSaleCount());
    }
}
