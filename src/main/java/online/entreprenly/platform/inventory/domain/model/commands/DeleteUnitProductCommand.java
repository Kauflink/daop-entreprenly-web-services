package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a unit product by its identifier.
 *
 * @param unitProductId the identifier of the product to delete
 */
public record DeleteUnitProductCommand(Long unitProductId) {
}
