package online.entreprenly.platform.inventory.domain.model.commands;

import java.time.Instant;

/**
 * Command to create a new unit lot.
 *
 * @param productId  the unit product this lot belongs to
 * @param codeQR     the QR code identifying the lot
 * @param entryDate  the date the lot entered stock
 * @param quantity   the number of units in the lot
 * @param expiryDate the lot expiry date
 */
public record CreateUnitLotCommand(Long productId, String codeQR, Instant entryDate, int quantity,
                                   Instant expiryDate) {
}
