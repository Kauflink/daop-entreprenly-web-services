package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.ChatMessagePersistenceEntity;


public final class ChatMessagePersistenceAssembler {

    private ChatMessagePersistenceAssembler() {
    }

    public static ChatMessage toDomainFromPersistence(ChatMessagePersistenceEntity entity) {
        if (entity == null) return null;
        var message = new ChatMessage();
        message.restoreState(
                entity.getId(),
                entity.getConversationId(),
                entity.getContent(),
                entity.getSender(),
                entity.getType(),
                entity.getSentAt());
        return message;
    }

    public static ChatMessagePersistenceEntity toPersistenceFromDomain(ChatMessage message) {
        if (message == null) return null;
        var entity = new ChatMessagePersistenceEntity();
        if (message.getId() != null) {
            entity.setId(message.getId());
        }
        entity.setConversationId(message.getConversationId());
        entity.setContent(message.getContent());
        entity.setSender(message.getSender());
        entity.setType(message.getType());
        entity.setSentAt(message.getSentAt());
        return entity;
    }
}
