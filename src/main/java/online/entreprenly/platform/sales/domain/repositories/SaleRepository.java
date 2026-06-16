package online.entreprenly.platform.sales.domain.repositories;

import online.entreprenly.platform.sales.domain.model.aggregates.Sale;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Sale repository port. All lookups are scoped to the owner account.
 */
public interface SaleRepository {
    List<Sale> findAllByOwnerEmail(String ownerEmail);

    List<Sale> findAllByOwnerEmailAndDate(String ownerEmail, LocalDate date);

    Optional<Sale> findByIdAndOwnerEmail(Long id, String ownerEmail);

    Sale save(Sale sale);
}
