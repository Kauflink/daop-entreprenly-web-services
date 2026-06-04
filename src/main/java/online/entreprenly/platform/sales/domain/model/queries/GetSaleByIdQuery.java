package online.entreprenly.platform.sales.domain.model.queries;

/**
 * Query to get a sale by its identifier.
 *
 * @param saleId the sale identifier
 */
public record GetSaleByIdQuery(Long saleId) {
}
