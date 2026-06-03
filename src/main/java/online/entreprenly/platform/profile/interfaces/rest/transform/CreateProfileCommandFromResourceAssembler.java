package online.entreprenly.platform.profile.interfaces.rest.transform;

import online.entreprenly.platform.profile.domain.model.commands.CreateProfileCommand;
import online.entreprenly.platform.profile.interfaces.rest.resources.CreateProfileResource;

/**
 * Assembler that translates {@link CreateProfileResource} into {@link CreateProfileCommand}.
 */
public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.role(),
                resource.plan(),
                resource.phone(),
                resource.timezone());
    }
}
