package online.entreprenly.platform.sales.domain.model.queries;

/**
 * Query to retrieve every cash register owned by the authenticated account.
 *
 * @param ownerEmail the authenticated account whose registers are requested
 */
public record GetAllCashRegistersQuery(String ownerEmail) {
}
