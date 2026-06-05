package online.entreprenly.platform.inventory.domain.model.commands;

import java.time.Instant;

/**
 * Command to create a new weight lot for an owner account.
 *
 * @param ownerEmail the email of the account that owns the lot
 * @param productId  the weight product this lot belongs to
 * @param codeQR     the QR code identifying the lot
 * @param entryDate  the date the lot entered stock
 * @param quantityKg the quantity in kilograms in the lot
 */
public record CreateWeightLotCommand(String ownerEmail, Long productId, String codeQR, Instant entryDate,
                                     double quantityKg) {
}
