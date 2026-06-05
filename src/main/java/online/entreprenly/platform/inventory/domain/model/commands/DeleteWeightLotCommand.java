package online.entreprenly.platform.inventory.domain.model.commands;

/**
 * Command to delete a weight lot by its identifier.
 *
 * @param weightLotId the identifier of the lot to delete
 */
public record DeleteWeightLotCommand(Long weightLotId) {
}
