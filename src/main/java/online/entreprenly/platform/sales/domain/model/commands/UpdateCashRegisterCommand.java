package online.entreprenly.platform.sales.domain.model.commands;

/**
 * Command to update the running totals of an existing cash register.
 *
 * @param cashRegisterId the cash register identifier
 * @param totalCash      the new cash total
 * @param totalDigital   the new digital total
 * @param saleCount      the new number of sales registered for the day
 */
public record UpdateCashRegisterCommand(Long cashRegisterId, double totalCash, double totalDigital, int saleCount) {
}
