package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a weight product owned by an account.
 *
 * @param ownerEmail      the email of the account that owns the product
 * @param weightProductId the identifier of the product to delete
 */
public record DeleteWeightProductCommand(String ownerEmail, Long weightProductId) {
}
