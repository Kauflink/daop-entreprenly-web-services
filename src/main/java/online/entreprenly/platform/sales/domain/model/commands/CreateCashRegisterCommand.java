package online.entreprenly.platform.sales.domain.model.commands;

import java.time.LocalDate;

/**
 * Command to open a cash register for a given business day.
 *
 * @param ownerEmail   the authenticated account that owns the register
 * @param date         the business day this register belongs to
 * @param totalCash    the initial cash total
 * @param totalDigital the initial digital total
 */
public record CreateCashRegisterCommand(String ownerEmail, LocalDate date, double totalCash, double totalDigital) {
}
