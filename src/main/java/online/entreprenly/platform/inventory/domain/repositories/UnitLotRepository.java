package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;

import java.util.List;
import java.util.Optional;

/**
 * Unit lot repository port.
 */
public interface UnitLotRepository {
    List<UnitLot> findAll();

    Optional<UnitLot> findById(Long id);

    boolean existsById(Long id);

    UnitLot save(UnitLot unitLot);

    void deleteById(Long id);
}
