package online.entreprenly.platform.profile.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing a profile returned by the REST API.
 */
@Schema(name = "ProfileResponse", description = "User profile information")
public record ProfileResource(
    @Schema(description = "Profile unique identifier", example = "1")
    Long id,

    @Schema(description = "IAM user identifier this profile belongs to", example = "1")
    Long userId,

    @Schema(description = "First name", example = "Entreprenly")
    String firstName,

    @Schema(description = "Last name", example = "INC")
    String lastName,

    @Schema(description = "Avatar URL", example = "https://cdn.entreprenly.online/avatars/1.png", nullable = true)
    String avatarUrl,

    @Schema(description = "Display role", example = "Administrador")
    String role,

    @Schema(description = "Display plan", example = "Plan Control")
    String plan,

    @Schema(description = "User preferences")
    PreferencesResource preferences,

    @Schema(description = "User notification settings")
    NotificationSettingsResource notificationSettings
) {
}
