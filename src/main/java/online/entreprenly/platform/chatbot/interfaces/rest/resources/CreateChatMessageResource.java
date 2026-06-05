package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * Request body to append a message to a conversation.
 */
@Schema(name = "CreateChatMessageRequest", description = "Data required to append a message")
public record CreateChatMessageResource(
        @NotNull
        @Schema(description = "Identifier of the owning conversation", example = "1")
        Long conversationId,

        @NotBlank
        @Schema(description = "Message content", example = "Hola, ¿qué deseas pedir hoy?")
        String content,

        @Schema(description = "Who authored the message", example = "bot", nullable = true)
        MessageSender sender,

        @Schema(description = "Content type", example = "text", nullable = true)
        MessageType type,

        @Schema(description = "Instant the message was sent", nullable = true)
        Instant sentAt
) {
}
