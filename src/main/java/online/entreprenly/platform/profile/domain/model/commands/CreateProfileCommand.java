package online.entreprenly.platform.profile.domain.model.commands;

/**
 * Command to create a profile for an IAM user.
 *
 * @param userId    the IAM user identifier this profile belongs to
 * @param firstName the user's first name
 * @param lastName  the user's last name
 * @param role      the display role (e.g. {@code Administrador})
 * @param plan      the display plan (e.g. {@code Plan Control})
 */
public record CreateProfileCommand(Long userId, String firstName, String lastName, String role, String plan) {
}
