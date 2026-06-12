package online.entreprenly.platform.iam.application.internal.eventhandlers;

import online.entreprenly.platform.iam.domain.model.events.UserSignedUpEvent;
import online.entreprenly.platform.iam.interfaces.events.UserSignedUpIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Internal application-layer handler for the {@link UserSignedUpEvent} domain event.
 *
 * <p>Translates the internal domain event into a {@link UserSignedUpIntegrationEvent} and
 * publishes it for cross-context consumers. Other bounded contexts must subscribe to the
 * integration event, never to the internal {@link UserSignedUpEvent}.</p>
 */
@Service("iamUserSignedUpEventHandler")
public class UserSignedUpEventHandler {

    private final ApplicationEventPublisher eventPublisher;

    public UserSignedUpEventHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Receives the internal {@link UserSignedUpEvent} and publishes the corresponding
     * {@link UserSignedUpIntegrationEvent} for cross-context consumers.
     *
     * @param event the internal user-signed-up domain event
     */
    @EventListener
    public void on(UserSignedUpEvent event) {
        eventPublisher.publishEvent(UserSignedUpIntegrationEvent.from(event));
    }
}
