package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;

import java.util.List;
import java.util.Optional;

/**
 * Weight product repository port. All lookups are scoped to the owner account.
 */
public interface WeightProductRepository {
    List<WeightProduct> findAllByOwnerEmail(String ownerEmail);

    Optional<WeightProduct> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByCodeQRAndOwnerEmail(String codeQR, String ownerEmail);

    WeightProduct save(WeightProduct weightProduct);

    void deleteById(Long id);
}
