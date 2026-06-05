package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a weight product by its identifier.
 *
 * @param weightProductId the weight product identifier
 */
public record GetWeightProductByIdQuery(Long weightProductId) {
}
