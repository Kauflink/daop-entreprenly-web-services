package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to create a new unit product for an owner account.
 *
 * @param ownerEmail  the email of the account that owns the product
 * @param name        the product display name
 * @param description the product description
 * @param codeQR      the QR code identifying the product
 * @param price       the unit price
 * @param weightGrams the per-unit weight in grams
 * @param brand       the product brand
 */
public record CreateUnitProductCommand(String ownerEmail, String name, String description, String codeQR,
                                       double price, double weightGrams, String brand) {
}
