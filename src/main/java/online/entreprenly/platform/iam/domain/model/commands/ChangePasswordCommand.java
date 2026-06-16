package online.entreprenly.platform.iam.domain.model.commands;

/**
 * Command to change the password of an authenticated user.
 *
 * @param email           the email of the user whose password is being changed (resolved from the JWT)
 * @param currentPassword the user's current password, verified before applying the change
 * @param newPassword     the new password to set
 */
public record ChangePasswordCommand(String email, String currentPassword, String newPassword) {
}
