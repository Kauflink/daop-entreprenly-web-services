package online.entreprenly.platform.inventory.interfaces.rest.transform;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.interfaces.rest.resources.WeightProductResource;

/**
 * Assembler that converts {@link WeightProduct} aggregates into {@link WeightProductResource} objects.
 */
public class WeightProductResourceFromEntityAssembler {

    public static WeightProductResource toResourceFromEntity(WeightProduct weightProduct) {
        return new WeightProductResource(
                weightProduct.getId(),
                weightProduct.getName(),
                weightProduct.getDescription(),
                weightProduct.getCodeQR(),
                weightProduct.getProductType().getValue(),
                weightProduct.getPricePerKg());
    }
}
