package online.entreprenly.platform.iam.interfaces.events;

import online.entreprenly.platform.iam.domain.model.events.UserSignedUpEvent;

/**
 * Integration event published by the IAM bounded context when a new user has signed up.
 *
 * <p>This is the <em>published language</em> of the IAM context. Other bounded contexts
 * (e.g. {@code profile}, {@code subscription}) must listen to this event rather than to the
 * internal domain event {@link UserSignedUpEvent}, which is an internal concern of the IAM
 * domain.</p>
 *
 * @param userId    the identifier of the newly created user
 * @param email     the email of the newly created user
 * @param firstName the first name provided at registration (nullable)
 * @param lastName  the last name provided at registration (nullable)
 * @param phone     the phone number provided at registration (nullable)
 * @param timezone  the preferred timezone provided at registration (nullable)
 */
public record UserSignedUpIntegrationEvent(Long userId, String email, String firstName, String lastName,
                                           String phone, String timezone) {

    /**
     * Convenience factory that maps the internal domain event to the integration event.
     *
     * @param event the internal user-signed-up domain event
     * @return a fully populated {@link UserSignedUpIntegrationEvent}
     */
    public static UserSignedUpIntegrationEvent from(UserSignedUpEvent event) {
        return new UserSignedUpIntegrationEvent(event.userId(), event.email(), event.firstName(),
                event.lastName(), event.phone(), event.timezone());
    }
}
