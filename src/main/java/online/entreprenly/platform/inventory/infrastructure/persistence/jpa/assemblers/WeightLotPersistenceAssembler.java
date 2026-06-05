package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.WeightLotPersistenceEntity;

/**
 * Static assembler between weight lot domain and persistence representations.
 */
public final class WeightLotPersistenceAssembler {

    private WeightLotPersistenceAssembler() {
    }

    public static WeightLot toDomainFromPersistence(WeightLotPersistenceEntity entity) {
        if (entity == null) return null;
        var weightLot = new WeightLot();
        weightLot.restoreState(
                entity.getId(),
                entity.getOwnerEmail(),
                entity.getProductId(),
                entity.getCodeQR(),
                entity.getEntryDate(),
                entity.getQuantityKg());
        return weightLot;
    }

    public static WeightLotPersistenceEntity toPersistenceFromDomain(WeightLot weightLot) {
        if (weightLot == null) return null;
        var entity = new WeightLotPersistenceEntity();
        if (weightLot.getId() != null) {
            entity.setId(weightLot.getId());
        }
        entity.setOwnerEmail(weightLot.getOwnerEmail());
        entity.setProductId(weightLot.getProductId());
        entity.setCodeQR(weightLot.getCodeQR());
        entity.setEntryDate(weightLot.getEntryDate());
        entity.setQuantityKg(weightLot.getQuantityKg());
        return entity;
    }
}
