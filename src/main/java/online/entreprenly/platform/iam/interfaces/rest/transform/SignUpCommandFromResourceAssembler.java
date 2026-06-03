package online.entreprenly.platform.iam.interfaces.rest.transform;

import online.entreprenly.platform.iam.domain.model.commands.SignUpCommand;
import online.entreprenly.platform.iam.interfaces.rest.resources.SignUpResource;

/**
 * Assembler that translates {@link SignUpResource} into {@link SignUpCommand}.
 */
public class SignUpCommandFromResourceAssembler {
    /**
     * Converts the incoming sign-up resource to an application command.
     *
     * @param resource sign-up payload from REST API
     * @return sign-up command consumed by the application layer
     */
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(resource.email(), resource.password(), resource.firstName(),
                resource.lastName(), resource.phone(), resource.timezone());
    }
}
