package online.entreprenly.platform.sales.domain.model.queries;

import java.time.LocalDate;

/**
 * Query to get the cash register that belongs to a given business day for the
 * authenticated account.
 *
 * @param ownerEmail the authenticated account that owns the register
 * @param date       the business day
 */
public record GetCashRegisterByDateQuery(String ownerEmail, LocalDate date) {
}
