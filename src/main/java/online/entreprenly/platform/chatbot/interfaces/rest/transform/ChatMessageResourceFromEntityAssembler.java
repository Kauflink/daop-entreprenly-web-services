package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.ChatMessageResource;


public final class ChatMessageResourceFromEntityAssembler {

    private ChatMessageResourceFromEntityAssembler() {
    }

    public static ChatMessageResource toResourceFromEntity(ChatMessage message) {
        return new ChatMessageResource(
                message.getId(),
                message.getConversationId(),
                message.getContent(),
                message.getSender(),
                message.getType(),
                message.getSentAt());
    }
}
