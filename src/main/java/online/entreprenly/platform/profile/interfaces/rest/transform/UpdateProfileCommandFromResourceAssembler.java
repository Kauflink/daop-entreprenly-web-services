package online.entreprenly.platform.profile.interfaces.rest.transform;

import online.entreprenly.platform.profile.domain.model.commands.UpdateProfileCommand;
import online.entreprenly.platform.profile.interfaces.rest.resources.UpdateProfileResource;

/**
 * Assembler that translates {@link UpdateProfileResource} (plus the path id) into
 * {@link UpdateProfileCommand}.
 */
public class UpdateProfileCommandFromResourceAssembler {
    public static UpdateProfileCommand toCommandFromResource(Long profileId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(profileId, resource.firstName(), resource.lastName(), resource.phone(), resource.avatarUrl());
    }
}
