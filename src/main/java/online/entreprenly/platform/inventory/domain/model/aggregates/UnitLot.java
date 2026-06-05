package online.entreprenly.platform.inventory.domain.model.aggregates;

import online.entreprenly.platform.inventory.domain.model.valueobjects.ProductType;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Unit lot aggregate root.
 *
 * <p>Represents a batch of a {@code UnitProduct} that entered stock on a given
 * {@code entryDate}, owned by the account ({@code ownerEmail}) that created it. It holds a
 * {@code quantity} of units and an {@code expiryDate}. Its {@link ProductType} is always
 * {@link ProductType#UNIT}.</p>
 */
@Getter
public class UnitLot extends AbstractDomainAggregateRoot<UnitLot> {

    @Setter
    private Long id;
    private String ownerEmail;
    private Long productId;
    private String codeQR;
    private Instant entryDate;
    private int quantity;
    private Instant expiryDate;

    public UnitLot() {
    }

    public UnitLot(String ownerEmail, Long productId, String codeQR, Instant entryDate, int quantity,
                   Instant expiryDate) {
        this.ownerEmail = ownerEmail;
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate == null ? Instant.now() : entryDate;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the measurement type of this lot, always {@link ProductType#UNIT}.
     *
     * @return the unit product type
     */
    public ProductType getLotType() {
        return ProductType.UNIT;
    }

    /**
     * Updates the editable attributes of this lot.
     *
     * @param productId  the new product identifier
     * @param codeQR     the new QR code
     * @param entryDate  the new entry date
     * @param quantity   the new quantity of units
     * @param expiryDate the new expiry date
     * @return this unit lot
     */
    public UnitLot updateInfo(Long productId, String codeQR, Instant entryDate, int quantity, Instant expiryDate) {
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        return this;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a unit lot that already carries identity and full state.
     */
    public void restoreState(Long id, String ownerEmail, Long productId, String codeQR, Instant entryDate,
                             int quantity, Instant expiryDate) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.productId = productId;
        this.codeQR = codeQR;
        this.entryDate = entryDate;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }
}
