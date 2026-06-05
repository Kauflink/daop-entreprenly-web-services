package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;

import java.util.List;
import java.util.Optional;

/**
 * Unit product repository port. All lookups are scoped to the owner account.
 */
public interface UnitProductRepository {
    List<UnitProduct> findAllByOwnerEmail(String ownerEmail);

    Optional<UnitProduct> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByCodeQRAndOwnerEmail(String codeQR, String ownerEmail);

    UnitProduct save(UnitProduct unitProduct);

    void deleteById(Long id);
}
