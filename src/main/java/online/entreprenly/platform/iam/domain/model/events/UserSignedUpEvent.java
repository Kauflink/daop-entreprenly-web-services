package online.entreprenly.platform.iam.domain.model.events;

/**
 * Domain event published when a new user successfully signs up.
 *
 * <p>Consumed by other bounded contexts (e.g. profile) to react to user
 * registration without coupling to IAM internals. Carries the optional profile
 * bootstrap data captured at registration time.</p>
 *
 * @param userId    the identifier of the newly created user
 * @param email     the email of the newly created user
 * @param firstName the first name provided at registration (nullable)
 * @param lastName  the last name provided at registration (nullable)
 * @param phone     the phone number provided at registration (nullable)
 * @param timezone  the preferred timezone provided at registration (nullable)
 */
public record UserSignedUpEvent(Long userId, String email, String firstName, String lastName,
                                String phone, String timezone) {
}
