package online.entreprenly.platform.sales.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * JPA persistence entity for cash registers. A business day is unique per owner account,
 * so each account keeps its own daily register.
 */
@Entity
@Table(name = "cash_registers", uniqueConstraints = @UniqueConstraint(
        name = "uk_cash_registers_owner_date", columnNames = {"owner_email", "register_date"}))
@Getter
@Setter
@NoArgsConstructor
public class CashRegisterPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "owner_email", nullable = false, length = 120)
    private String ownerEmail;

    @Column(name = "register_date", nullable = false)
    private LocalDate date;

    @Column(name = "total_cash", nullable = false)
    private double totalCash;

    @Column(name = "total_digital", nullable = false)
    private double totalDigital;

    @Column(name = "sale_count", nullable = false)
    private int saleCount;
}
