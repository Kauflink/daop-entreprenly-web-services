package online.entreprenly.platform.iam.interfaces.rest.transform;

import online.entreprenly.platform.iam.domain.model.commands.ChangeEmailCommand;
import online.entreprenly.platform.iam.interfaces.rest.resources.ChangeEmailResource;

/**
 * Assembler that builds a {@link ChangeEmailCommand} from a {@link ChangeEmailResource}
 * and the authenticated user's current email (resolved from the JWT).
 */
public class ChangeEmailCommandFromResourceAssembler {
    public static ChangeEmailCommand toCommandFromResource(String currentEmail, ChangeEmailResource resource) {
        return new ChangeEmailCommand(currentEmail, resource.email());
    }
}
