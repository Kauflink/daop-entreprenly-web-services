package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a unit product owned by an account.
 *
 * @param ownerEmail    the email of the account that owns the product
 * @param unitProductId the identifier of the product to delete
 */
public record DeleteUnitProductCommand(String ownerEmail, Long unitProductId) {
}
