package online.entreprenly.platform.profile.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing notification settings, used both in profile responses and as the
 * request body of the update-notification-settings endpoint.
 */
@Schema(name = "NotificationSettings", description = "User notification settings")
public record NotificationSettingsResource(
    @Schema(description = "Whether stock alerts are enabled", example = "true")
    boolean stockAlerts
) {
}
