package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a weight lot by its identifier, scoped to its owner account.
 *
 * @param ownerEmail  the email of the account that owns the lot
 * @param weightLotId the weight lot identifier
 */
public record GetWeightLotByIdQuery(String ownerEmail, Long weightLotId) {
}
