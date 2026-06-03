package online.entreprenly.platform.profile.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource representing user preferences, used both in profile responses and as the
 * request body of the update-preferences endpoint.
 */
@Schema(name = "Preferences", description = "User preferences")
public record PreferencesResource(
    @Schema(description = "UI language code", example = "es")
    @NotBlank
    @Size(max = 10)
    String language,

    @Schema(description = "Display timezone", example = "America/Lima (UTC-05:00)")
    @NotBlank
    @Size(max = 60)
    String timezone,

    @Schema(description = "UI theme", example = "light")
    @NotBlank
    @Size(max = 20)
    String theme,

    @Schema(description = "Preferred currency code", example = "PEN")
    @NotBlank
    @Size(max = 10)
    String currency
) {
}
