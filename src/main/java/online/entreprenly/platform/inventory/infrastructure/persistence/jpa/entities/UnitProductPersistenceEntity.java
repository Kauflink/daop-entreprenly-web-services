package online.entreprenly.platform.inventory.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for unit products.
 */
@Entity
@Table(name = "inventory_unit_products")
@Getter
@Setter
@NoArgsConstructor
public class UnitProductPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "code_qr", length = 255)
    private String codeQR;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "weight_grams", nullable = false)
    private double weightGrams;

    @Column(name = "brand", length = 160)
    private String brand;
}
