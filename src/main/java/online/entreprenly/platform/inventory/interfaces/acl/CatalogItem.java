package online.entreprenly.platform.inventory.interfaces.acl;

/**
 * Published catalog snapshot of an inventory product, exposed to other bounded contexts.
 *
 * @param name     the product display name
 * @param price    the unit price for unit products, or the price per kilogram for weight products
 * @param byWeight whether the product is sold by weight
 * @param stock    the currently available stock (units or kilograms)
 */
public record CatalogItem(String name, double price, boolean byWeight, double stock) {
}
