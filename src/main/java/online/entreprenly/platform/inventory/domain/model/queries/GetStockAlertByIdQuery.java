package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get a currently raised stock alert by its (run-scoped) identifier for an account.
 *
 * @param ownerEmail   the email of the account whose alerts are requested
 * @param stockAlertId the stock alert identifier
 */
public record GetStockAlertByIdQuery(String ownerEmail, Long stockAlertId) {
}
