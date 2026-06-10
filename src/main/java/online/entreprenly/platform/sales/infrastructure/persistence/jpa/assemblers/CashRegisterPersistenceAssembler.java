package online.entreprenly.platform.sales.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities.CashRegisterPersistenceEntity;

/**
 * Static assembler between cash register domain and persistence representations.
 */
public final class CashRegisterPersistenceAssembler {

    private CashRegisterPersistenceAssembler() {
    }

    public static CashRegister toDomainFromPersistence(CashRegisterPersistenceEntity entity) {
        if (entity == null) return null;
        var cashRegister = new CashRegister();
        cashRegister.restoreState(
                entity.getId(),
                entity.getOwnerEmail(),
                entity.getDate(),
                entity.getTotalCash(),
                entity.getTotalDigital(),
                entity.getSaleCount());
        return cashRegister;
    }

    public static CashRegisterPersistenceEntity toPersistenceFromDomain(CashRegister cashRegister) {
        if (cashRegister == null) return null;
        var entity = new CashRegisterPersistenceEntity();
        if (cashRegister.getId() != null) {
            entity.setId(cashRegister.getId());
        }
        entity.setOwnerEmail(cashRegister.getOwnerEmail());
        entity.setDate(cashRegister.getDate());
        entity.setTotalCash(cashRegister.getTotalCash());
        entity.setTotalDigital(cashRegister.getTotalDigital());
        entity.setSaleCount(cashRegister.getSaleCount());
        return entity;
    }
}
