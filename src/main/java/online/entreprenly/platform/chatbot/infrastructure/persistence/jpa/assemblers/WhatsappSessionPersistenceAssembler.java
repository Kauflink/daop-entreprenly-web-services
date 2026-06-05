package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities.WhatsappSessionPersistenceEntity;

/**
 * Static assembler between WhatsApp session domain and persistence representations.
 */
public final class WhatsappSessionPersistenceAssembler {

    private WhatsappSessionPersistenceAssembler() {
    }

    public static WhatsappSession toDomainFromPersistence(WhatsappSessionPersistenceEntity entity) {
        if (entity == null) return null;
        var session = new WhatsappSession();
        session.restoreState(
                entity.getId(),
                entity.getSellerId(),
                entity.getPhone(),
                entity.getBusinessName(),
                entity.getStatus(),
                entity.getConnectedAt(),
                entity.getQrCode());
        return session;
    }

    public static WhatsappSessionPersistenceEntity toPersistenceFromDomain(WhatsappSession session) {
        if (session == null) return null;
        var entity = new WhatsappSessionPersistenceEntity();
        if (session.getId() != null) {
            entity.setId(session.getId());
        }
        entity.setSellerId(session.getSellerId());
        entity.setPhone(session.getPhone());
        entity.setBusinessName(session.getBusinessName());
        entity.setStatus(session.getStatus());
        entity.setConnectedAt(session.getConnectedAt());
        entity.setQrCode(session.getQrCode());
        return entity;
    }
}
