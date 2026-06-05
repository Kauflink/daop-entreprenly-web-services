package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a weight product by its identifier.
 *
 * @param weightProductId the identifier of the product to delete
 */
public record DeleteWeightProductCommand(Long weightProductId) {
}
