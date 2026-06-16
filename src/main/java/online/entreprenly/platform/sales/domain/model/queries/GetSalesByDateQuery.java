package online.entreprenly.platform.sales.domain.model.queries;

import java.time.LocalDate;

/**
 * Query to retrieve the sales registered by the authenticated account on a given business day.
 *
 * @param ownerEmail the authenticated account whose sales are requested
 * @param date       the business day (local date) used to filter the sales
 */
public record GetSalesByDateQuery(String ownerEmail, LocalDate date) {
}
