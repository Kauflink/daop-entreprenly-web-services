package online.entreprenly.platform.inventory.domain.repositories;

import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;

import java.util.List;
import java.util.Optional;

/**
 * Weight lot repository port. All lookups are scoped to the owner account.
 */
public interface WeightLotRepository {
    List<WeightLot> findAllByOwnerEmail(String ownerEmail);

    Optional<WeightLot> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);

    WeightLot save(WeightLot weightLot);

    void deleteById(Long id);
}
