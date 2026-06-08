package online.entreprenly.platform.sales.domain.model.queries;

/**
 * Query to retrieve every sale registered by the authenticated account.
 *
 * @param ownerEmail the authenticated account whose sales are requested
 */
public record GetAllSalesQuery(String ownerEmail) {
}
