package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.ConversationResource;


public final class ConversationResourceFromEntityAssembler {

    private ConversationResourceFromEntityAssembler() {
    }

    public static ConversationResource toResourceFromEntity(Conversation conversation) {
        return new ConversationResource(
                conversation.getId(),
                conversation.getSellerId(),
                conversation.getClientPhone(),
                conversation.getClientName(),
                conversation.getLastMessage(),
                conversation.getLastMessageTime(),
                conversation.getStatus(),
                conversation.getCreatedAt(),
                conversation.getClosedAt());
    }
}
