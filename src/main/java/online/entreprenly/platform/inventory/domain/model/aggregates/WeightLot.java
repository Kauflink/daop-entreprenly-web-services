package online.entreprenly.platform.inventory.domain.model.aggregates;

import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Weight lot aggregate root.
 *
 * <p>Represents a batch of a {@code WeightProduct} that entered stock on a given
 * {@code entryDate}, owned by the account ({@code ownerEmail}) that created it. It holds a
 * {@code quantityKg} in kilograms. Its {@link ProductType} is always
 * {@link ProductType#WEIGHT}.</p>
 */
@Getter
public class WeightLot extends AbstractDomainAggregateRoot<WeightLot> {

    @Setter
    private Long id;
    private String ownerEmail;
    private Long productId;
    private String codeQR;
    private Instant entryDate;
    private double quantityKg;

    public WeightLot() {
    }

    public WeightLot(String ownerEmail, Long productId, String codeQR, Instant entryDate, double quantityKg) {
        this.ownerEmail = ownerEmail;
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
     * Removes up to {@code amountKg} kilograms from this lot, never going below zero.
     *
     * @param amountKg the kilograms requested for removal
     * @return the kilograms actually removed (capped at the available quantity)
     */
    public double consume(double amountKg) {
        if (amountKg <= 0) return 0;
        double removed = Math.min(amountKg, this.quantityKg);
        this.quantityKg = Math.round((this.quantityKg - removed) * 1000.0) / 1000.0;
        return removed;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a weight lot that already carries identity and full state.
     */
    public void restoreState(Long id, String ownerEmail, Long productId, String codeQR, Instant entryDate,
                             double quantityKg) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate;
        this.quantityKg = quantityKg;
    }
}
