package online.entreprenly.platform.profile.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.profile.domain.model.aggregates.Profile;
import online.entreprenly.platform.profile.domain.model.valueobjects.NotificationSettings;
import online.entreprenly.platform.profile.domain.model.valueobjects.Preferences;
import online.entreprenly.platform.profile.infrastructure.persistence.jpa.embeddables.NotificationSettingsEmbeddable;
import online.entreprenly.platform.profile.infrastructure.persistence.jpa.embeddables.PreferencesEmbeddable;
import online.entreprenly.platform.profile.infrastructure.persistence.jpa.entities.ProfilePersistenceEntity;

/**
 * Static assembler between profile domain and persistence representations.
 */
public final class ProfilePersistenceAssembler {

    private ProfilePersistenceAssembler() {
    }

    public static Profile toDomainFromPersistence(ProfilePersistenceEntity entity) {
        if (entity == null) return null;
        var preferences = entity.getPreferences() == null
                ? Preferences.defaults()
                : new Preferences(
                        entity.getPreferences().getLanguage(),
                        entity.getPreferences().getTimezone(),
                        entity.getPreferences().getTheme(),
                        entity.getPreferences().getCurrency());
        var notificationSettings = entity.getNotificationSettings() == null
                ? NotificationSettings.defaults()
                : new NotificationSettings(entity.getNotificationSettings().isStockAlerts());
        var profile = new Profile();
        profile.restoreState(
                entity.getId(),
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getAvatarUrl(),
                entity.getRole(),
                entity.getPlan(),
                preferences,
                notificationSettings);
        return profile;
    }

    public static ProfilePersistenceEntity toPersistenceFromDomain(Profile profile) {
        if (profile == null) return null;
        var entity = new ProfilePersistenceEntity();
        if (profile.getId() != null) {
            entity.setId(profile.getId());
        }
        entity.setUserId(profile.getUserId());
        entity.setFirstName(profile.getFirstName());
        entity.setLastName(profile.getLastName());
        entity.setPhone(profile.getPhone());
        entity.setAvatarUrl(profile.getAvatarUrl());
        entity.setRole(profile.getRole());
        entity.setPlan(profile.getPlan());
        var prefs = profile.getPreferences() == null ? Preferences.defaults() : profile.getPreferences();
        entity.setPreferences(new PreferencesEmbeddable(
                prefs.language(), prefs.timezone(), prefs.theme(), prefs.currency()));
        var notif = profile.getNotificationSettings() == null
                ? NotificationSettings.defaults() : profile.getNotificationSettings();
        entity.setNotificationSettings(new NotificationSettingsEmbeddable(notif.stockAlerts()));
        return entity;
    }
}
