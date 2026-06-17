package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


@Schema(name = "UpdateWhatsappSessionRequest", description = "Data required to change a session's status")
public record UpdateWhatsappSessionResource(
        @NotNull
        @Schema(description = "Desired connection status", example = "connected")
        SessionStatus status
) {
}
