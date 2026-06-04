package online.entreprenly.platform.sales.domain.repositories;

import online.entreprenly.platform.sales.domain.model.aggregates.Sale;

import java.util.List;
import java.util.Optional;

/**
 * Sale repository port.
 */
public interface SaleRepository {
    List<Sale> findAll();

    Optional<Sale> findById(Long id);

    Sale save(Sale sale);
}
