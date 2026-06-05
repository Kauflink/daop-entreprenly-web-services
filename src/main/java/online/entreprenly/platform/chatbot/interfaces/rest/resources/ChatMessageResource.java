package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a chat message returned by the REST API.
 */
@Schema(name = "ChatMessageResponse", description = "A message within a conversation")
public record ChatMessageResource(
        @Schema(description = "Message unique identifier", example = "1")
        Long id,

        @Schema(description = "Identifier of the owning conversation", example = "1")
        Long conversationId,

        @Schema(description = "Message content", example = "Hola, ¿qué deseas pedir hoy?")
        String content,

        @Schema(description = "Who authored the message", example = "bot")
        MessageSender sender,

        @Schema(description = "Content type", example = "text")
        MessageType type,

        @Schema(description = "Instant the message was sent")
        Instant sentAt
) {
}
