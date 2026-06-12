package online.entreprenly.platform.inventory.interfaces.acl;

/**
 * A quantity of a named product to deduct from inventory stock, used by other bounded
 * contexts through the {@link InventoryContextFacade}.
 *
 * @param productName the product display name, matched case-insensitively
 * @param quantity    the quantity to deduct (units for unit products, kilograms for weight products)
 */
public record StockDeductionItem(String productName, int quantity) {
}
