package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to update an existing unit product owned by an account.
 *
 * @param ownerEmail    the email of the account that owns the product
 * @param unitProductId the identifier of the product to update
 * @param name          the new display name
 * @param description   the new description
 * @param codeQR        the new QR code
 * @param price         the new unit price
 * @param weightGrams   the new per-unit weight in grams
 * @param brand         the new brand
 */
public record UpdateUnitProductCommand(String ownerEmail, Long unitProductId, String name, String description,
                                       String codeQR, double price, double weightGrams, String brand) {
}
