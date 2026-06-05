package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a unit product by its identifier, scoped to its owner account.
 *
 * @param ownerEmail    the email of the account that owns the product
 * @param unitProductId the unit product identifier
 */
public record GetUnitProductByIdQuery(String ownerEmail, Long unitProductId) {
}
