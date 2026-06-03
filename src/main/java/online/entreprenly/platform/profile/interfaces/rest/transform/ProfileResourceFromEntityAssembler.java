package online.entreprenly.platform.profile.interfaces.rest.transform;

import online.entreprenly.platform.profile.domain.model.aggregates.Profile;
import online.entreprenly.platform.profile.interfaces.rest.resources.NotificationSettingsResource;
import online.entreprenly.platform.profile.interfaces.rest.resources.PreferencesResource;
import online.entreprenly.platform.profile.interfaces.rest.resources.ProfileResource;

/**
 * Assembler that converts {@link Profile} aggregates into {@link ProfileResource} objects.
 */
public class ProfileResourceFromEntityAssembler {
    /**
     * Converts a profile aggregate to its REST representation.
     *
     * @param profile profile aggregate
     * @return profile resource
     */
    public static ProfileResource toResourceFromEntity(Profile profile) {
        var preferences = new PreferencesResource(
                profile.getPreferences().language(),
                profile.getPreferences().timezone(),
                profile.getPreferences().theme(),
                profile.getPreferences().currency());
        var notificationSettings = new NotificationSettingsResource(
                profile.getNotificationSettings().stockAlerts(),
                profile.getNotificationSettings().paymentAlerts(),
                profile.getNotificationSettings().chatbotMessages());
        return new ProfileResource(
                profile.getId(),
                profile.getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getAvatarUrl(),
                profile.getRole(),
                profile.getPlan(),
                preferences,
                notificationSettings);
    }
}
