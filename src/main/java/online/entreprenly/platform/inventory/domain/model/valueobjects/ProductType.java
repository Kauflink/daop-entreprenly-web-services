package online.entreprenly.platform.inventory.domain.model.valueobjects;

/**
 * Discriminates how an inventory item is measured and sold.
 *
 * <p>{@code UNIT} items are tracked and priced per unit, while {@code WEIGHT}
 * items are tracked and priced by weight (kilograms). The wire value exposed to
 * clients is the lowercase {@link #getValue() code} ({@code "unit"} / {@code "weight"}),
 * matching the inventory frontend contract.</p>
 */
public enum ProductType {
    UNIT("unit"),
    WEIGHT("weight");

    private final String value;

    ProductType(String value) {
        this.value = value;
    }

    /**
     * Returns the lowercase wire value of this product type.
     *
     * @return {@code "unit"} or {@code "weight"}
     */
    public String getValue() {
        return value;
    }
}
