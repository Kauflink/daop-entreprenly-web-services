package online.entreprenly.platform.sales.domain.model.queries;

import java.time.LocalDate;

/**
 * Query to get the cash register that belongs to a given business day.
 *
 * @param date the business day
 */
public record GetCashRegisterByDateQuery(LocalDate date) {
}
