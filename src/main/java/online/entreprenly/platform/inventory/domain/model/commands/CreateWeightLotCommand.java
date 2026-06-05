package online.entreprenly.platform.inventory.domain.model.commands;

import java.time.Instant;

/**
 * Command to create a new weight lot.
 *
 * @param productId  the weight product this lot belongs to
 * @param codeQR     the QR code identifying the lot
 * @param entryDate  the date the lot entered stock
 * @param quantityKg the quantity in kilograms in the lot
 */
public record CreateWeightLotCommand(Long productId, String codeQR, Instant entryDate, double quantityKg) {
}
