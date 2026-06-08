package online.entreprenly.platform.sales.domain.model.queries;

/**
 * Query to get a sale by its identifier, scoped to the authenticated account.
 *
 * @param ownerEmail the authenticated account that owns the sale
 * @param saleId     the sale identifier
 */
public record GetSaleByIdQuery(String ownerEmail, Long saleId) {
}
