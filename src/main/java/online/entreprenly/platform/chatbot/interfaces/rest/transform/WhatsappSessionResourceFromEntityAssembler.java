package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.WhatsappSessionResource;


public final class WhatsappSessionResourceFromEntityAssembler {

    private WhatsappSessionResourceFromEntityAssembler() {
    }

    public static WhatsappSessionResource toResourceFromEntity(WhatsappSession session) {
        return new WhatsappSessionResource(
                session.getId(),
                session.getSellerId(),
                session.getPhone(),
                session.getBusinessName(),
                session.getStatus(),
                session.getConnectedAt(),
                session.getQrCode());
    }
}
