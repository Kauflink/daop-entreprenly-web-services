package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a unit product by its identifier.
 *
 * @param unitProductId the unit product identifier
 */
public record GetUnitProductByIdQuery(Long unitProductId) {
}
