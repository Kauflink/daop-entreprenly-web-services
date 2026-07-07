package online.entreprenly.platform.sales.application.internal.outboundservices.acl;

import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;

import java.util.List;

/**
 * Outbound port through which the Sales context deducts the sold quantities from inventory,
 * without depending on another bounded context's internal model. Implemented in the
 * infrastructure layer by an ACL adapter.
 */
public interface StockDeductionService {

    void decrementForSale(String ownerEmail, List<SaleItem> items);
}
