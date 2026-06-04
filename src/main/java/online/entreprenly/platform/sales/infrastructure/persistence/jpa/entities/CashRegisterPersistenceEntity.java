package online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * JPA persistence entity for cash registers.
 */
@Entity
@Table(name = "cash_registers")
@Getter
@Setter
@NoArgsConstructor
public class CashRegisterPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "register_date", nullable = false, unique = true)
    private LocalDate date;

    @Column(name = "total_cash", nullable = false)
    private double totalCash;

    @Column(name = "total_digital", nullable = false)
    private double totalDigital;

    @Column(name = "sale_count", nullable = false)
    private int saleCount;
}
