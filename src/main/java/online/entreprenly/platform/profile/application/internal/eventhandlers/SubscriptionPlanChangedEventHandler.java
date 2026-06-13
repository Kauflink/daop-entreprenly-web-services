package online.entreprenly.platform.profile.application.internal.eventhandlers;

import online.entreprenly.platform.profile.application.commandservices.ProfileCommandService;
import online.entreprenly.platform.profile.domain.model.commands.UpdateProfilePlanCommand;
import online.entreprenly.platform.subscription.interfaces.events.SubscriptionPlanChangedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for {@link SubscriptionPlanChangedIntegrationEvent} from the Subscription context and
 * keeps the profile's display plan in sync, so the profile screen reflects the active plan.
 */
@Component
@Slf4j
public class SubscriptionPlanChangedEventHandler {

    private final ProfileCommandService profileCommandService;

    public SubscriptionPlanChangedEventHandler(ProfileCommandService profileCommandService) {
        this.profileCommandService = profileCommandService;
    }

    /**
     * Updates the profile display plan when a user's subscription plan changes.
     *
     * @param event the plan-changed integration event published by the Subscription context
     */
    @EventListener
    public void on(SubscriptionPlanChangedIntegrationEvent event) {
        var command = new UpdateProfilePlanCommand(event.userId(), event.planName());
        var result = profileCommandService.handle(command);
        if (result.isFailure()) {
            log.warn("Could not sync profile plan for user {}: profile not found", event.userId());
        } else {
            log.info("Profile plan synced to '{}' for user {}", event.planName(), event.userId());
        }
    }
}
