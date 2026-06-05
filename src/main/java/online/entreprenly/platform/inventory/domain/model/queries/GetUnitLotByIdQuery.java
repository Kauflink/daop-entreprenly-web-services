package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a unit lot by its identifier.
 *
 * @param unitLotId the unit lot identifier
 */
public record GetUnitLotByIdQuery(Long unitLotId) {
}
