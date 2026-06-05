package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA persistence entity for unit lots.
 */
@Entity
@Table(name = "inventory_unit_lots")
@Getter
@Setter
@NoArgsConstructor
public class UnitLotPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "code_qr", length = 255)
    private String codeQR;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "expiry_date")
    private Instant expiryDate;
}
