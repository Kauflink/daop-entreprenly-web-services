package online.entreprenly.platform.profile.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource received to update the editable fields of a profile.
 */
@Schema(name = "UpdateProfileRequest", description = "Request to update a user profile")
public record UpdateProfileResource(
    @Schema(description = "First name", example = "Entreprenly")
    @NotBlank
    @Size(max = 80)
    String firstName,

    @Schema(description = "Last name", example = "INC")
    @NotBlank
    @Size(max = 80)
    String lastName,

    @Schema(description = "Phone number", example = "+51 999 888 777", nullable = true)
    @Size(max = 30)
    String phone,

    @Schema(description = "Avatar URL", example = "https://cdn.entreprenly.online/avatars/1.png", nullable = true)
    @Size(max = 512)
    String avatarUrl
) {
}
