package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;

import java.util.List;
import java.util.Optional;

/**
 * Unit product repository port.
 */
public interface UnitProductRepository {
    List<UnitProduct> findAll();

    Optional<UnitProduct> findById(Long id);

    boolean existsById(Long id);

    boolean existsByCodeQR(String codeQR);

    UnitProduct save(UnitProduct unitProduct);

    void deleteById(Long id);
}
