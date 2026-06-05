package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.UnitLotPersistenceEntity;

/**
 * Static assembler between unit lot domain and persistence representations.
 */
public final class UnitLotPersistenceAssembler {

    private UnitLotPersistenceAssembler() {
    }

    public static UnitLot toDomainFromPersistence(UnitLotPersistenceEntity entity) {
        if (entity == null) return null;
        var unitLot = new UnitLot();
        unitLot.restoreState(
                entity.getId(),
                entity.getOwnerEmail(),
                entity.getProductId(),
                entity.getCodeQR(),
                entity.getEntryDate(),
                entity.getQuantity(),
                entity.getExpiryDate());
        return unitLot;
    }

    public static UnitLotPersistenceEntity toPersistenceFromDomain(UnitLot unitLot) {
        if (unitLot == null) return null;
        var entity = new UnitLotPersistenceEntity();
        if (unitLot.getId() != null) {
            entity.setId(unitLot.getId());
        }
        entity.setOwnerEmail(unitLot.getOwnerEmail());
        entity.setProductId(unitLot.getProductId());
        entity.setCodeQR(unitLot.getCodeQR());
        entity.setEntryDate(unitLot.getEntryDate());
        entity.setQuantity(unitLot.getQuantity());
        entity.setExpiryDate(unitLot.getExpiryDate());
        return entity;
    }
}
