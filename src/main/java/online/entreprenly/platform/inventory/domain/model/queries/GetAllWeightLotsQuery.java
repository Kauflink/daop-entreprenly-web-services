package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get every weight lot owned by an account.
 *
 * @param ownerEmail the email of the account whose lots are requested
 */
public record GetAllWeightLotsQuery(String ownerEmail) {
}
