package online.entreprenly.platform.profile.interfaces.rest.transform;

import online.entreprenly.platform.profile.domain.model.commands.UpdatePreferencesCommand;
import online.entreprenly.platform.profile.interfaces.rest.resources.PreferencesResource;

/**
 * Assembler that translates {@link PreferencesResource} (plus the path id) into
 * {@link UpdatePreferencesCommand}.
 */
public class UpdatePreferencesCommandFromResourceAssembler {
    public static UpdatePreferencesCommand toCommandFromResource(Long profileId, PreferencesResource resource) {
        return new UpdatePreferencesCommand(
                profileId, resource.language(), resource.timezone(), resource.theme(), resource.currency());
    }
}
