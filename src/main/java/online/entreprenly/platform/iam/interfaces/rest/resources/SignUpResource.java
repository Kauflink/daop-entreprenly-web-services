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
    String password,

    @Schema(description = "First name", example = "Lionel", nullable = true)
    @Size(max = 80)
    String firstName,

    @Schema(description = "Last name", example = "Gutierrez", nullable = true)
    @Size(max = 80)
    String lastName,

    @Schema(description = "Phone number", example = "+51 999 888 777", nullable = true)
    @Size(max = 30)
    String phone,

    @Schema(description = "Preferred timezone", example = "America/Lima (UTC-05:00)", nullable = true)
    @Size(max = 60)
    String timezone
) {
}
