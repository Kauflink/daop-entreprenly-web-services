package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get every unit lot owned by an account.
 *
 * @param ownerEmail the email of the account whose lots are requested
 */
public record GetAllUnitLotsQuery(String ownerEmail) {
}
