package online.entreprenly.platform.iam.domain.model.commands;

/**
 * Sign up command
 * <p>
 *     This class represents the command to register a new user. The default role
 *     ({@code ROLE_USER}) is assigned by the application service; the client does
 *     not choose roles at registration time.
 * </p>
 * @param email the email of the user
 * @param password the password of the user
 *
 * @see online.entreprenly.platform.iam.domain.model.aggregates.User
 */
public record SignUpCommand(String email, String password) {
}
