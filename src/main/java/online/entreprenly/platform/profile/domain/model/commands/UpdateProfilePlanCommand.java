package online.entreprenly.platform.profile.domain.model.commands;

/**
 * Command to update the display plan of the profile that belongs to a user.
 *
 * @param userId the IAM user identifier the profile belongs to
 * @param plan   the display plan name (e.g. {@code Plan Control})
 */
public record UpdateProfilePlanCommand(Long userId, String plan) {
}
