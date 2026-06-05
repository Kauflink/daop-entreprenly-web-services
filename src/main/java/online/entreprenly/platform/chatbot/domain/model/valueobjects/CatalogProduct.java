package online.entreprenly.platform.chatbot.domain.model.valueobjects;

/**
 * Read model of a seller's catalog product, as seen by the chatbot.
 *
 * <p>It is a faithful, context-local snapshot built from the Inventory bounded context
 * (unit or weight products plus their lots). The chatbot reasons over this value object
 * to answer the client about price and availability, without depending on the inventory
 * domain model directly.</p>
 *
 * @param name           the product display name
 * @param price          the unit price, or the price per kilogram when {@code soldByWeight}
 * @param soldByWeight   whether the product is sold by weight (kg) instead of per unit
 * @param availableStock the available stock (units, or kilograms when {@code soldByWeight})
 */
public record CatalogProduct(String name, double price, boolean soldByWeight, double availableStock) {

    /**
     * @return {@code true} when there is at least some stock available
     */
    public boolean isInStock() {
        return availableStock > 0;
    }
}
