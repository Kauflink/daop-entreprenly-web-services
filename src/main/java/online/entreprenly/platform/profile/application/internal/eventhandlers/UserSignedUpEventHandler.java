package online.entreprenly.platform.profile.application.internal.eventhandlers;

import online.entreprenly.platform.iam.domain.model.events.UserSignedUpEvent;
import online.entreprenly.platform.profile.application.commandservices.ProfileCommandService;
import online.entreprenly.platform.profile.domain.model.commands.CreateProfileCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for {@link UserSignedUpEvent} from the IAM context and provisions a
 * default profile for the newly registered user.
 */
@Component
@Slf4j
public class UserSignedUpEventHandler {

    private static final String DEFAULT_ROLE = "User";
    private static final String DEFAULT_PLAN = "Free";

    private final ProfileCommandService profileCommandService;

    public UserSignedUpEventHandler(ProfileCommandService profileCommandService) {
        this.profileCommandService = profileCommandService;
    }

    /**
     * Creates a default profile when a user signs up. The first name is derived from the
     * email's local part; the user can complete the profile later.
     *
     * @param event the user-signed-up event published by the IAM context
     */
    @EventListener
    public void on(UserSignedUpEvent event) {
        var firstName = deriveFirstName(event.email());
        var command = new CreateProfileCommand(event.userId(), firstName, "", DEFAULT_ROLE, DEFAULT_PLAN);
        var result = profileCommandService.handle(command);
        if (result.isFailure()) {
            log.warn("Could not auto-create profile for user {}: profile may already exist", event.userId());
        } else {
            log.info("Default profile created for user {}", event.userId());
        }
    }

    private String deriveFirstName(String email) {
        if (email == null || email.isBlank()) return "";
        int at = email.indexOf('@');
        return at > 0 ? email.substring(0, at) : email;
    }
}
