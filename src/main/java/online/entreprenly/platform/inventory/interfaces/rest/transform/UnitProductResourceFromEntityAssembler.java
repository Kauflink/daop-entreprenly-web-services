package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UnitProductResource;

/**
 * Assembler that converts {@link UnitProduct} aggregates into {@link UnitProductResource} objects.
 */
public class UnitProductResourceFromEntityAssembler {

    public static UnitProductResource toResourceFromEntity(UnitProduct unitProduct) {
        return new UnitProductResource(
                unitProduct.getId(),
                unitProduct.getName(),
                unitProduct.getDescription(),
                unitProduct.getCodeQR(),
                unitProduct.getProductType().getValue(),
                unitProduct.getPrice(),
                unitProduct.getWeightGrams(),
                unitProduct.getBrand());
    }
}
