package online.entreprenly.platform.profile.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Resource received to create a profile for an IAM user.
 */
@Schema(name = "CreateProfileRequest", description = "Request to create a user profile")
public record CreateProfileResource(
    @Schema(description = "IAM user identifier this profile belongs to", example = "1")
    @NotNull
    Long userId,

    @Schema(description = "First name", example = "Entreprenly")
    @NotBlank
    @Size(max = 80)
    String firstName,

    @Schema(description = "Last name", example = "INC")
    @NotBlank
    @Size(max = 80)
    String lastName,

    @Schema(description = "Display role", example = "Administrador")
    @Size(max = 60)
    String role,

    @Schema(description = "Display plan", example = "Plan Control")
    @Size(max = 60)
    String plan,

    @Schema(description = "Phone number", example = "+51 999 888 777", nullable = true)
    @Size(max = 30)
    String phone,

    @Schema(description = "Preferred display timezone", example = "America/Lima (UTC-05:00)", nullable = true)
    @Size(max = 60)
    String timezone
) {
}
