package online.entreprenly.platform.inventory.domain.model.aggregates;

import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Weight lot aggregate root.
 *
 * <p>Represents a batch of a {@link WeightProduct} that entered stock on a given
 * {@code entryDate}, holding a {@code quantityKg} in kilograms. Its
 * {@link ProductType} is always {@link ProductType#WEIGHT}.</p>
 */
@Getter
public class WeightLot extends AbstractDomainAggregateRoot<WeightLot> {

    @Setter
    private Long id;
    private Long productId;
    private String codeQR;
    private Instant entryDate;
    private double quantityKg;

    public WeightLot() {
    }

    public WeightLot(Long productId, String codeQR, Instant entryDate, double quantityKg) {
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate == null ? Instant.now() : entryDate;
        this.quantityKg = quantityKg;
    }

    /**
     * Returns the measurement type of this lot, always {@link ProductType#WEIGHT}.
     *
     * @return the weight product type
     */
    public ProductType getLotType() {
        return ProductType.WEIGHT;
    }

    /**
     * Updates the editable attributes of this lot.
     *
     * @param productId  the new product identifier
     * @param codeQR     the new QR code
     * @param entryDate  the new entry date
     * @param quantityKg the new quantity in kilograms
     * @return this weight lot
     */
    public WeightLot updateInfo(Long productId, String codeQR, Instant entryDate, double quantityKg) {
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate;
        this.quantityKg = quantityKg;
        return this;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a weight lot that already carries identity and full state.
     */
    public void restoreState(Long id, Long productId, String codeQR, Instant entryDate, double quantityKg) {
        this.id = id;
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate;
        this.quantityKg = quantityKg;
    }
}
