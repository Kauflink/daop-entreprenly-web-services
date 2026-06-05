package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a weight lot by its identifier.
 *
 * @param weightLotId the weight lot identifier
 */
public record GetWeightLotByIdQuery(Long weightLotId) {
}
