package online.entreprenly.platform.profile.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable persistence representation of the user's preferences.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PreferencesEmbeddable {

    @Column(name = "preference_language", length = 10)
    private String language;

    @Column(name = "preference_timezone", length = 60)
    private String timezone;

    @Column(name = "preference_theme", length = 20)
    private String theme;

    @Column(name = "preference_currency", length = 10)
    private String currency;

    public PreferencesEmbeddable(String language, String timezone, String theme, String currency) {
        this.language = language;
        this.timezone = timezone;
        this.theme = theme;
        this.currency = currency;
    }
}
