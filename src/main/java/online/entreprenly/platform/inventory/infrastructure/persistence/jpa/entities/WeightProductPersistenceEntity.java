package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for weight products.
 */
@Entity
@Table(name = "inventory_weight_products")
@Getter
@Setter
@NoArgsConstructor
public class WeightProductPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "owner_email", nullable = false, length = 120)
    private String ownerEmail;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "code_qr", length = 255)
    private String codeQR;

    @Column(name = "price_per_kg", nullable = false)
    private double pricePerKg;
}
