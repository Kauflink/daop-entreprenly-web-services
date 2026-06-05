package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to create a new weight product.
 *
 * @param name        the product display name
 * @param description the product description
 * @param codeQR      the QR code identifying the product
 * @param pricePerKg  the price per kilogram
 */
public record CreateWeightProductCommand(String name, String description, String codeQR, double pricePerKg) {
}
