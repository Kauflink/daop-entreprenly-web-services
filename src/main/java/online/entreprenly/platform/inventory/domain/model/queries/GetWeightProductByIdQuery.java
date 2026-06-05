package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a weight product by its identifier, scoped to its owner account.
 *
 * @param ownerEmail      the email of the account that owns the product
 * @param weightProductId the weight product identifier
 */
public record GetWeightProductByIdQuery(String ownerEmail, Long weightProductId) {
}
