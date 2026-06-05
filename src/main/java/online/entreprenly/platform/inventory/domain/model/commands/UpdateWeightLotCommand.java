package online.entreprenly.platform.inventory.domain.model.commands;

import java.time.Instant;

/**
 * Command to update an existing weight lot owned by an account.
 *
 * @param ownerEmail  the email of the account that owns the lot
 * @param weightLotId the identifier of the lot to update
 * @param productId   the weight product this lot belongs to
 * @param codeQR      the QR code identifying the lot
 * @param entryDate   the date the lot entered stock
 * @param quantityKg  the quantity in kilograms in the lot
 */
public record UpdateWeightLotCommand(String ownerEmail, Long weightLotId, Long productId, String codeQR,
                                     Instant entryDate, double quantityKg) {
}
