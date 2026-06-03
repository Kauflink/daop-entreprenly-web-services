package online.entreprenly.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource received to register a new IAM user. The default role is assigned by
 * the server, so the client only provides credentials.
 */
@Schema(
    name = "SignUpRequest",
    description = "User sign-up request with credentials",
    example = "{\"email\": \"john.doe@example.com\", \"password\": \"SecurePass123!\"}"
)
public record SignUpResource(
    @Schema(
        description = "User email",
        example = "john.doe@example.com",
        maxLength = 120
    )
    @NotBlank
    @Email
    @Size(max = 120)
    String email,

    @Schema(
        description = "User password (minimum 8 characters)",
        example = "SecurePass123!",
        minLength = 8,
        maxLength = 255
    )
    @NotBlank
    @Size(min = 8, max = 255)
    String password
) {
}
