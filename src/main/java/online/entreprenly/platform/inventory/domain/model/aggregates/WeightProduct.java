package online.entreprenly.platform.inventory.domain.model.aggregates;

import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

/**
 * Weight product aggregate root.
 *
 * <p>Represents a catalog product that is tracked and sold by weight, owned by the account
 * ({@code ownerEmail}) that created it. Its commercial attribute is the {@code pricePerKg}.
 * Its {@link ProductType} is always {@link ProductType#WEIGHT}.</p>
 */
@Getter
public class WeightProduct extends AbstractDomainAggregateRoot<WeightProduct> {

    @Setter
    private Long id;
    private String ownerEmail;
    private String name;
    private String description;
    private String codeQR;
    private double pricePerKg;

    public WeightProduct() {
    }

    public WeightProduct(String ownerEmail, String name, String description, String codeQR, double pricePerKg) {
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
        this.codeQR = codeQR;
        this.pricePerKg = pricePerKg;
    }

    /**
     * Returns the measurement type of this product, always {@link ProductType#WEIGHT}.
     *
     * @return the weight product type
     */
    public ProductType getProductType() {
        return ProductType.WEIGHT;
    }

    /**
     * Updates the editable attributes of this product.
     *
     * @param name        the new display name
     * @param description the new description
     * @param codeQR      the new QR code
     * @param pricePerKg  the new price per kilogram
     * @return this weight product
     */
    public WeightProduct updateInfo(String name, String description, String codeQR, double pricePerKg) {
        this.name = name;
        this.description = description;
        this.codeQR = codeQR;
        this.pricePerKg = pricePerKg;
        return this;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a weight product that already carries identity and full state.
     */
    public void restoreState(Long id, String ownerEmail, String name, String description, String codeQR,
                             double pricePerKg) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
        this.codeQR = codeQR;
        this.pricePerKg = pricePerKg;
    }
}
