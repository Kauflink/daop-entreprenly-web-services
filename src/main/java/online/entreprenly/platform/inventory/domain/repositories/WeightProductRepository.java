package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;

import java.util.List;
import java.util.Optional;

/**
 * Weight product repository port.
 */
public interface WeightProductRepository {
    List<WeightProduct> findAll();

    Optional<WeightProduct> findById(Long id);

    boolean existsById(Long id);

    boolean existsByCodeQR(String codeQR);

    WeightProduct save(WeightProduct weightProduct);

    void deleteById(Long id);
}
