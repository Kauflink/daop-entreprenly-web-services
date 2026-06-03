package online.entreprenly.platform.iam.domain.model.events;

/**
 * Domain event published when a new user successfully signs up.
 *
 * <p>Consumed by other bounded contexts (e.g. profile) to react to user
 * registration without coupling to IAM internals.</p>
 *
 * @param userId the identifier of the newly created user
 * @param email  the email of the newly created user
 */
public record UserSignedUpEvent(Long userId, String email) {
}
