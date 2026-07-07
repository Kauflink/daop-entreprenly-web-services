package online.entreprenly.platform.sales.domain.model.valueobjects;

/**
 * A sellable product for the point of sale, with its currently available stock.
 *
 * <p>Owned by the Sales context so the catalog it consumes from other contexts is not
 * expressed in their internal types.</p>
 *
 * @param name    the product display name
 * @param price   the unit price for unit products, or the price per kilogram for weight products
 * @param byWeight whether the product is sold by weight
 * @param stock   the currently available stock (units or kilograms)
 */
public record CatalogProduct(String name, double price, boolean byWeight, double stock) {
}
