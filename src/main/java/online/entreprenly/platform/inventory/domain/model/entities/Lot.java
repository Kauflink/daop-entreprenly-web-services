package online.entreprenly.platform.inventory.domain.model.entities;

import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import lombok.Getter;

import java.time.Instant;

/**
 * Generic lot read model.
 *
 * <p>Provides a unified, read-only view over both {@code UnitLot} and {@code WeightLot}
 * aggregates, exposing only the attributes they share. It is never persisted; it is
 * assembled on demand so clients can list every stock batch regardless of its
 * measurement {@link ProductType}.</p>
 */
@Getter
public class Lot {

    private final Long id;
    private final Long productId;
    private final String codeQR;
    private final Instant entryDate;
    private final ProductType lotType;

    public Lot(Long id, Long productId, String codeQR, Instant entryDate, ProductType lotType) {
        this.id = id;
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate;
        this.lotType = lotType;
    }
}
