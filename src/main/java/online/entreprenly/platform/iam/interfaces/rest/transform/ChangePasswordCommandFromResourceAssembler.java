package online.entreprenly.platform.iam.interfaces.rest.transform;

import online.entreprenly.platform.iam.domain.model.commands.ChangePasswordCommand;
import online.entreprenly.platform.iam.interfaces.rest.resources.ChangePasswordResource;

/**
 * Assembler that builds a {@link ChangePasswordCommand} from a {@link ChangePasswordResource}
 * and the authenticated user's email (resolved from the JWT).
 */
public class ChangePasswordCommandFromResourceAssembler {
    public static ChangePasswordCommand toCommandFromResource(String email, ChangePasswordResource resource) {
        return new ChangePasswordCommand(email, resource.currentPassword(), resource.newPassword());
    }
}
