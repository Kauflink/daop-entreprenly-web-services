package online.entreprenly.platform.profile.application.commandservices;

import online.entreprenly.platform.profile.domain.model.aggregates.Profile;
import online.entreprenly.platform.profile.domain.model.commands.CreateProfileCommand;
import online.entreprenly.platform.profile.domain.model.commands.UpdateNotificationSettingsCommand;
import online.entreprenly.platform.profile.domain.model.commands.UpdatePreferencesCommand;
import online.entreprenly.platform.profile.domain.model.commands.UpdateProfileCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;

/**
 * Application service contract for profile commands.
 */
public interface ProfileCommandService {
    /**
     * Creates a profile for an IAM user.
     *
     * @param command create command
     * @return created profile, or an application error
     */
    Result<Profile, ApplicationError> handle(CreateProfileCommand command);

    /**
     * Updates the editable fields of a profile.
     *
     * @param command update command
     * @return updated profile, or an application error
     */
    Result<Profile, ApplicationError> handle(UpdateProfileCommand command);

    /**
     * Updates a profile's preferences.
     *
     * @param command update-preferences command
     * @return updated profile, or an application error
     */
    Result<Profile, ApplicationError> handle(UpdatePreferencesCommand command);

    /**
     * Updates a profile's notification settings.
     *
     * @param command update-notification-settings command
     * @return updated profile, or an application error
     */
    Result<Profile, ApplicationError> handle(UpdateNotificationSettingsCommand command);
}
