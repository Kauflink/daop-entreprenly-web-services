package online.entreprenly.platform.profile.domain.model.commands;

/**
 * Command to update a profile's notification settings.
 *
 * @param profileId   the profile identifier
 * @param stockAlerts whether stock alerts are enabled
 */
public record UpdateNotificationSettingsCommand(Long profileId, boolean stockAlerts) {
}
