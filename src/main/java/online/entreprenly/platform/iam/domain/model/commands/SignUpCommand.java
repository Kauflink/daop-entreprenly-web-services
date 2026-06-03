package online.entreprenly.platform.iam.domain.model.commands;

/**
 * Sign up command
 * <p>
 *     This class represents the command to register a new user. The default role
 *     ({@code ROLE_USER}) is assigned by the application service; the client does
 *     not choose roles at registration time. The remaining fields are optional
 *     profile bootstrap data forwarded to the profile context.
 * </p>
 * @param email the email of the user
 * @param password the password of the user
 * @param firstName the user's first name (nullable)
 * @param lastName the user's last name (nullable)
 * @param phone the user's phone number (nullable)
 * @param timezone the user's preferred timezone (nullable)
 *
 * @see online.entreprenly.platform.iam.domain.model.aggregates.User
 */
public record SignUpCommand(String email, String password, String firstName, String lastName,
                            String phone, String timezone) {
}
