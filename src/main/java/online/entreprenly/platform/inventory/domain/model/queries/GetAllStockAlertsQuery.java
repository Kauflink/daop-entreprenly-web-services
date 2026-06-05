package online.entreprenly.platform.inventory.domain.model.queries;

/**
 * Query to get every currently raised stock alert for an account.
 *
 * @param ownerEmail the email of the account whose alerts are requested
 */
public record GetAllStockAlertsQuery(String ownerEmail) {
}
