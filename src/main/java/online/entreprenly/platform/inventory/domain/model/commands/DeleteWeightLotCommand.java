package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a weight lot owned by an account.
 *
 * @param ownerEmail  the email of the account that owns the lot
 * @param weightLotId the identifier of the lot to delete
 */
public record DeleteWeightLotCommand(String ownerEmail, Long weightLotId) {
}
