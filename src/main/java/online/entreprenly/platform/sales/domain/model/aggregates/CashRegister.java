package online.entreprenly.platform.sales.domain.model.aggregates;

import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Cash register aggregate root.
 *
 * <p>Aggregates the takings of a single business day, split between cash and digital
 * payments, together with the number of sales registered that day. {@code totalDay}
 * is derived from both totals.</p>
 */
@Getter
public class CashRegister extends AbstractDomainAggregateRoot<CashRegister> {

    @Setter
    private Long id;
    private LocalDate date;
    private double totalCash;
    private double totalDigital;
    private int saleCount;

    public CashRegister() {
    }

    public CashRegister(LocalDate date, double totalCash, double totalDigital, int saleCount) {
        this.date = date;
        this.totalCash = totalCash;
        this.totalDigital = totalDigital;
        this.saleCount = saleCount;
    }

    /**
     * Returns the total takings of the day (cash plus digital).
     *
     * @return the rounded total for the day
     */
    public double getTotalDay() {
        return Math.round((totalCash + totalDigital) * 100.0) / 100.0;
    }

    /**
     * Replaces the running totals and sale count of this register.
     *
     * @param totalCash    the new cash total
     * @param totalDigital the new digital total
     * @param saleCount    the new number of sales
     * @return this cash register
     */
    public CashRegister updateTotals(double totalCash, double totalDigital, int saleCount) {
        this.totalCash = totalCash;
        this.totalDigital = totalDigital;
        this.saleCount = saleCount;
        return this;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a cash register that already carries identity and full state.
     */
    public void restoreState(Long id, LocalDate date, double totalCash, double totalDigital, int saleCount) {
        this.id = id;
        this.date = date;
        this.totalCash = totalCash;
        this.totalDigital = totalDigital;
        this.saleCount = saleCount;
    }
}
