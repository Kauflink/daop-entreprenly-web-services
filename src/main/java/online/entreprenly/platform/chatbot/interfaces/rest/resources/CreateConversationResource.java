package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Schema(name = "CreateConversationRequest", description = "Data required to start a conversation")
public record CreateConversationResource(
        @NotNull
        @Schema(description = "Identifier of the owning seller", example = "1")
        Long sellerId,

        @NotBlank
        @Schema(description = "Client phone number", example = "+51 987 654 321")
        String clientPhone,

        @Schema(description = "Client display name", example = "Andrea Torres")
        String clientName
) {
}
