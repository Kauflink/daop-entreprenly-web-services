package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.UnitProductPersistenceEntity;

/**
 * Static assembler between unit product domain and persistence representations.
 */
public final class UnitProductPersistenceAssembler {

    private UnitProductPersistenceAssembler() {
    }

    public static UnitProduct toDomainFromPersistence(UnitProductPersistenceEntity entity) {
        if (entity == null) return null;
        var unitProduct = new UnitProduct();
        unitProduct.restoreState(
                entity.getId(),
                entity.getOwnerEmail(),
                entity.getName(),
                entity.getDescription(),
                entity.getCodeQR(),
                entity.getPrice(),
                entity.getWeightGrams(),
                entity.getBrand());
        return unitProduct;
    }

    public static UnitProductPersistenceEntity toPersistenceFromDomain(UnitProduct unitProduct) {
        if (unitProduct == null) return null;
        var entity = new UnitProductPersistenceEntity();
        if (unitProduct.getId() != null) {
            entity.setId(unitProduct.getId());
        }
        entity.setOwnerEmail(unitProduct.getOwnerEmail());
        entity.setName(unitProduct.getName());
        entity.setDescription(unitProduct.getDescription());
        entity.setCodeQR(unitProduct.getCodeQR());
        entity.setPrice(unitProduct.getPrice());
        entity.setWeightGrams(unitProduct.getWeightGrams());
        entity.setBrand(unitProduct.getBrand());
        return entity;
    }
}
