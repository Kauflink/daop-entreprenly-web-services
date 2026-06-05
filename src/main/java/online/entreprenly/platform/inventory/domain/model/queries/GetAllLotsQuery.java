package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get every registered lot of an account, regardless of its measurement type.
 *
 * @param ownerEmail the email of the account whose lots are requested
 */
public record GetAllLotsQuery(String ownerEmail) {
}
