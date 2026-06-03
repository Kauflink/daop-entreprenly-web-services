package online.entreprenly.platform.sales.application.internal.queryservices;

import online.entreprenly.platform.sales.application.queryservices.SaleQueryService;
import online.entreprenly.platform.sales.domain.model.aggregates.Sale;
import online.entreprenly.platform.sales.domain.model.queries.GetAllSalesQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetSaleByIdQuery;
import online.entreprenly.platform.sales.domain.repositories.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves sale read queries.
 */
@Service
public class SaleQueryServiceImpl implements SaleQueryService {

    private final SaleRepository saleRepository;

    public SaleQueryServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sale> handle(GetAllSalesQuery query) {
        return saleRepository.findAll();
    }

    @Override
    public Optional<Sale> handle(GetSaleByIdQuery query) {
        return saleRepository.findById(query.saleId());
    }
}
