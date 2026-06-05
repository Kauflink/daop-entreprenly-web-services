package online.entreprenly.platform.inventory.domain.model.aggregates;

import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

/**
 * Unit product aggregate root.
 *
 * <p>Represents a catalog product that is tracked and sold per unit. It belongs to the
 * account ({@code ownerEmail}) that created it, so inventory is isolated per tenant. It
 * holds the commercial attributes used both in the storefront and at the point of sale:
 * {@code price}, the per-unit {@code weightGrams} and the {@code brand}. Its
 * {@link ProductType} is always {@link ProductType#UNIT}.</p>
 */
@Getter
public class UnitProduct extends AbstractDomainAggregateRoot<UnitProduct> {

    @Setter
    private Long id;
    private String ownerEmail;
    private String name;
    private String description;
    private String codeQR;
    private double price;
    private double weightGrams;
    private String brand;

    public UnitProduct() {
    }

    public UnitProduct(String ownerEmail, String name, String description, String codeQR, double price,
                       double weightGrams, String brand) {
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
        this.codeQR = codeQR;
        this.price = price;
        this.weightGrams = weightGrams;
        this.brand = brand;
    }

    /**
     * Returns the measurement type of this product, always {@link ProductType#UNIT}.
     *
     * @return the unit product type
     */
    public ProductType getProductType() {
        return ProductType.UNIT;
    }

    /**
     * Updates the editable attributes of this product.
     *
     * @param name        the new display name
     * @param description the new description
     * @param codeQR      the new QR code
     * @param price       the new unit price
     * @param weightGrams the new per-unit weight in grams
     * @param brand       the new brand
     * @return this unit product
     */
    public UnitProduct updateInfo(String name, String description, String codeQR, double price,
                                  double weightGrams, String brand) {
        this.name = name;
        this.description = description;
        this.codeQR = codeQR;
        this.price = price;
        this.weightGrams = weightGrams;
        this.brand = brand;
        return this;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a unit product that already carries identity and full state.
     */
    public void restoreState(Long id, String ownerEmail, String name, String description, String codeQR,
                             double price, double weightGrams, String brand) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
        this.codeQR = codeQR;
        this.price = price;
        this.weightGrams = weightGrams;
        this.brand = brand;
    }
}
