package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;

import java.util.List;
import java.util.Optional;

/**
 * Unit lot repository port. All lookups are scoped to the owner account.
 */
public interface UnitLotRepository {
    List<UnitLot> findAllByOwnerEmail(String ownerEmail);

    Optional<UnitLot> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);

    UnitLot save(UnitLot unitLot);

    void deleteById(Long id);
}
