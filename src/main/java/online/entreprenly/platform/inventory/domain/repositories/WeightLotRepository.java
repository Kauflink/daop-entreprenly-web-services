package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;

import java.util.List;
import java.util.Optional;

/**
 * Weight lot repository port.
 */
public interface WeightLotRepository {
    List<WeightLot> findAll();

    Optional<WeightLot> findById(Long id);

    boolean existsById(Long id);

    WeightLot save(WeightLot weightLot);

    void deleteById(Long id);
}
