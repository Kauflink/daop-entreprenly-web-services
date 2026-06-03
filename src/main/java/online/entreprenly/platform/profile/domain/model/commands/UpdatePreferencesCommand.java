package online.entreprenly.platform.profile.domain.model.commands;

/**
 * Command to update a profile's preferences.
 *
 * @param profileId the profile identifier
 * @param language  the UI language code
 * @param timezone  the display timezone
 * @param theme     the UI theme
 * @param currency  the preferred currency code
 */
public record UpdatePreferencesCommand(Long profileId, String language, String timezone, String theme, String currency) {
}
