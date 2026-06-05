package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to update an existing weight product.
 *
 * @param weightProductId the identifier of the product to update
 * @param name            the new display name
 * @param description     the new description
 * @param codeQR          the new QR code
 * @param pricePerKg      the new price per kilogram
 */
public record UpdateWeightProductCommand(Long weightProductId, String name, String description, String codeQR,
                                         double pricePerKg) {
}
