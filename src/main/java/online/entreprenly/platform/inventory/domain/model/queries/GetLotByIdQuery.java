package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a lot by its identifier from the combined lot view, scoped to its owner account.
 *
 * @param ownerEmail the email of the account that owns the lot
 * @param lotId      the lot identifier
 */
public record GetLotByIdQuery(String ownerEmail, Long lotId) {
}
