package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Resource representing a conversation returned by the REST API.
 */
@Schema(name = "ConversationResponse", description = "A WhatsApp conversation with a client")
public record ConversationResource(
        @Schema(description = "Conversation unique identifier", example = "1")
        Long id,

        @Schema(description = "Identifier of the owning seller", example = "1")
        Long sellerId,

        @Schema(description = "Client phone number", example = "+51 987 654 321")
        String clientPhone,

        @Schema(description = "Client display name", example = "Andrea Torres")
        String clientName,

        @Schema(description = "Last message preview", example = "Comprobante enviado")
        String lastMessage,

        @Schema(description = "Last message time label", example = "10:18")
        String lastMessageTime,

        @Schema(description = "Conversation status", example = "WAITING_PAYMENT")
        ConversationStatus status,

        @Schema(description = "Instant the conversation was created")
        Instant createdAt,

        @Schema(description = "Instant the conversation was closed", nullable = true)
        Instant closedAt
) {
}
