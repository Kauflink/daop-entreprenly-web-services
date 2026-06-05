package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a unit lot owned by an account.
 *
 * @param ownerEmail the email of the account that owns the lot
 * @param unitLotId  the identifier of the lot to delete
 */
public record DeleteUnitLotCommand(String ownerEmail, Long unitLotId) {
}
