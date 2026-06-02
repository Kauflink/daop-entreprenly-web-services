package online.entreprenly.platform.profile.domain.model.aggregates;

import online.entreprenly.platform.profile.domain.model.valueobjects.NotificationSettings;
import online.entreprenly.platform.profile.domain.model.valueobjects.Preferences;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

/**
 * Profile aggregate root.
 *
 * <p>Holds the user-facing profile data for an IAM user (referenced by {@code userId}),
 * together with the user's {@link Preferences} and {@link NotificationSettings}.</p>
 */
@Getter
public class Profile extends AbstractDomainAggregateRoot<Profile> {

    @Setter
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String role;
    private String plan;
    private Preferences preferences;
    private NotificationSettings notificationSettings;

    protected Profile() {
    }

    public Profile(Long userId, String firstName, String lastName, String role, String plan) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = null;
        this.role = role;
        this.plan = plan;
        this.preferences = Preferences.defaults();
        this.notificationSettings = NotificationSettings.defaults();
    }

    /**
     * Updates the editable profile fields.
     *
     * @param firstName new first name
     * @param lastName  new last name
     * @param avatarUrl new avatar URL (nullable)
     * @return this profile
     */
    public Profile updateProfile(String firstName, String lastName, String avatarUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        return this;
    }

    /**
     * Replaces the user's preferences.
     *
     * @param preferences new preferences
     * @return this profile
     */
    public Profile updatePreferences(Preferences preferences) {
        this.preferences = preferences;
        return this;
    }

    /**
     * Replaces the user's notification settings.
     *
     * @param notificationSettings new notification settings
     * @return this profile
     */
    public Profile updateNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
        return this;
    }

    /**
     * Restores an aggregate from persistence. Used by assemblers when reconstructing
     * a profile that already carries identity and full state.
     */
    public void restoreState(Long id, Long userId, String firstName, String lastName, String avatarUrl,
                             String role, String plan, Preferences preferences,
                             NotificationSettings notificationSettings) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.plan = plan;
        this.preferences = preferences;
        this.notificationSettings = notificationSettings;
    }
}
