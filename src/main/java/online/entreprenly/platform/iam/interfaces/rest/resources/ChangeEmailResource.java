package online.entreprenly.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource received to change the email of the authenticated user.
 */
@Schema(
    name = "ChangeEmailRequest",
    description = "Request to change the authenticated user's email",
    example = "{\"email\": \"new-email@example.com\"}"
)
public record ChangeEmailResource(
    @Schema(description = "New email to set", maxLength = 120)
    @NotBlank
    @Email
    @Size(max = 120)
    String email
) {
}
