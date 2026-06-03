package online.entreprenly.platform.profile.domain.model.valueobjects;

/**
 * User preferences value object.
 *
 * @param language UI language code (e.g. {@code en}, {@code es})
 * @param timezone display timezone
 * @param theme    UI theme (e.g. {@code light}, {@code dark})
 * @param currency preferred currency code (e.g. {@code USD}, {@code PEN})
 */
public record Preferences(String language, String timezone, String theme, String currency) {

    /**
     * Returns the default preferences applied to a freshly created profile.
     *
     * @return default preferences
     */
    public static Preferences defaults() {
        return new Preferences("en", "UTC", "light", "USD");
    }
}
