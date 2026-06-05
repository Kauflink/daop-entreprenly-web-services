package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a unit lot by its identifier, scoped to its owner account.
 *
 * @param ownerEmail the email of the account that owns the lot
 * @param unitLotId  the unit lot identifier
 */
public record GetUnitLotByIdQuery(String ownerEmail, Long unitLotId) {
}
