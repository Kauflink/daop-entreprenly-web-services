package online.entreprenly.platform.iam.interfaces.acl;

/**
 * ACL facade that exposes IAM bounded context capabilities to other contexts.
 *
 * <p>Provides a simplified integration surface for creating users and querying identity data
 * without leaking IAM internal model details.</p>
 */
public interface IamContextFacade {
    /**
     * Creates a new user with the default role.
     *
     * @param email    email to register
     * @param password raw password
     * @return created user identifier, or {@code 0L} when creation fails
     */
    Long createUser(String email, String password);

    /**
     * Fetches the identifier for an email.
     *
     * @param email email to search
     * @return user identifier, or {@code 0L} when user is not found
     */
    Long fetchUserIdByEmail(String email);

    /**
     * Fetches the email for a user identifier.
     *
     * @param userId user identifier
     * @return email, or an empty string when user is not found
     */
    String fetchEmailByUserId(Long userId);
}
