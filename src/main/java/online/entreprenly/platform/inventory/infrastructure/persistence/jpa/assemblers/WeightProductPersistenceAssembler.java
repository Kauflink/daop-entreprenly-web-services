package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities.WeightProductPersistenceEntity;

/**
 * Static assembler between weight product domain and persistence representations.
 */
public final class WeightProductPersistenceAssembler {

    private WeightProductPersistenceAssembler() {
    }

    public static WeightProduct toDomainFromPersistence(WeightProductPersistenceEntity entity) {
        if (entity == null) return null;
        var weightProduct = new WeightProduct();
        weightProduct.restoreState(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCodeQR(),
                entity.getPricePerKg());
        return weightProduct;
    }

    public static WeightProductPersistenceEntity toPersistenceFromDomain(WeightProduct weightProduct) {
        if (weightProduct == null) return null;
        var entity = new WeightProductPersistenceEntity();
        if (weightProduct.getId() != null) {
            entity.setId(weightProduct.getId());
        }
        entity.setName(weightProduct.getName());
        entity.setDescription(weightProduct.getDescription());
        entity.setCodeQR(weightProduct.getCodeQR());
        entity.setPricePerKg(weightProduct.getPricePerKg());
        return entity;
    }
}
