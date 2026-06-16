package online.entreprenly.platform.iam.domain.model.commands;

/**
 * Command to change the email (login identity) of an authenticated user.
 *
 * @param currentEmail the user's current email (resolved from the JWT)
 * @param newEmail     the new email to set
 */
public record ChangeEmailCommand(String currentEmail, String newEmail) {
}
