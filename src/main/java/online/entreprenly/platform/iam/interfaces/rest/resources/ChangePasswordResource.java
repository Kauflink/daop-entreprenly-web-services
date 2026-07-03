package online.entreprenly.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource received to change the password of the authenticated user.
 */
@Schema(
    name = "ChangePasswordRequest",
    description = "Request to change the authenticated user's password",
    example = "{\"currentPassword\": \"OldPass123!\", \"newPassword\": \"NewPass123!\"}"
)
public record ChangePasswordResource(
    @Schema(description = "User's current password", minLength = 8, maxLength = 255)
    @NotBlank
    @Size(min = 8, max = 255)
    String currentPassword,

    @Schema(description = "New password to set", minLength = 8, maxLength = 255)
    @NotBlank
    @Size(min = 8, max = 255)
    String newPassword
) {
}
