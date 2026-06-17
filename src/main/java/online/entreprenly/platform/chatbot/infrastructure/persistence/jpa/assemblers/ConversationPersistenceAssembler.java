package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.ConversationPersistenceEntity;


public final class ConversationPersistenceAssembler {

    private ConversationPersistenceAssembler() {
    }

    public static Conversation toDomainFromPersistence(ConversationPersistenceEntity entity) {
        if (entity == null) return null;
        var conversation = new Conversation();
        conversation.restoreState(
                entity.getId(),
                entity.getSellerId(),
                entity.getClientPhone(),
                entity.getClientName(),
                entity.getLastMessage(),
                entity.getLastMessageTime(),
                entity.getStatus(),
                entity.getConversationCreatedAt(),
                entity.getClosedAt());
        return conversation;
    }

    public static ConversationPersistenceEntity toPersistenceFromDomain(Conversation conversation) {
        if (conversation == null) return null;
        var entity = new ConversationPersistenceEntity();
        if (conversation.getId() != null) {
            entity.setId(conversation.getId());
        }
        entity.setSellerId(conversation.getSellerId());
        entity.setClientPhone(conversation.getClientPhone());
        entity.setClientName(conversation.getClientName());
        entity.setLastMessage(conversation.getLastMessage());
        entity.setLastMessageTime(conversation.getLastMessageTime());
        entity.setStatus(conversation.getStatus());
        entity.setConversationCreatedAt(conversation.getCreatedAt());
        entity.setClosedAt(conversation.getClosedAt());
        return entity;
    }
}
