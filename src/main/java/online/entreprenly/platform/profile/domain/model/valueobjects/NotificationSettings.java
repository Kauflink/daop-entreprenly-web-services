package online.entreprenly.platform.profile.domain.model.valueobjects;

/**
 * Notification settings value object.
 *
 * @param stockAlerts whether stock alerts are enabled
 */
public record NotificationSettings(boolean stockAlerts) {

    /**
     * Returns the default notification settings applied to a freshly created profile.
     *
     * @return default notification settings
     */
    public static NotificationSettings defaults() {
        return new NotificationSettings(true);
    }
}
