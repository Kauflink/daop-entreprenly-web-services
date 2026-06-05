package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a unit lot by its identifier.
 *
 * @param unitLotId the identifier of the lot to delete
 */
public record DeleteUnitLotCommand(Long unitLotId) {
}
