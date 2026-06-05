package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a lot by its identifier from the combined lot view.
 *
 * @param lotId the lot identifier
 */
public record GetLotByIdQuery(Long lotId) {
}
