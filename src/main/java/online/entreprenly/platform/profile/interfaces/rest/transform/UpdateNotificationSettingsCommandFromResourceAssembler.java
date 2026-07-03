package online.entreprenly.platform.profile.interfaces.rest.transform;

import online.entreprenly.platform.profile.domain.model.commands.UpdateNotificationSettingsCommand;
import online.entreprenly.platform.profile.interfaces.rest.resources.NotificationSettingsResource;

/**
 * Assembler that translates {@link NotificationSettingsResource} (plus the path id) into
 * {@link UpdateNotificationSettingsCommand}.
 */
public class UpdateNotificationSettingsCommandFromResourceAssembler {
    public static UpdateNotificationSettingsCommand toCommandFromResource(Long profileId, NotificationSettingsResource resource) {
        return new UpdateNotificationSettingsCommand(profileId, resource.stockAlerts());
    }
}
