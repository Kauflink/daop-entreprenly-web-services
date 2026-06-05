package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a currently raised stock alert by its (run-scoped) identifier.
 *
 * @param stockAlertId the stock alert identifier
 */
public record GetStockAlertByIdQuery(Long stockAlertId) {
}
