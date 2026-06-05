package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get every weight product owned by an account.
 *
 * @param ownerEmail the email of the account whose products are requested
 */
public record GetAllWeightProductsQuery(String ownerEmail) {
}
