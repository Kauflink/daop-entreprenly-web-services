package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "UpdateConversationRequest", description = "Data required to update a conversation")
public record UpdateConversationResource(
        @Schema(description = "Desired status", example = "COMPLETED", nullable = true)
        ConversationStatus status,

        @Schema(description = "Last message preview", nullable = true)
        String lastMessage,

        @Schema(description = "Last message time label", nullable = true)
        String lastMessageTime
) {
}
