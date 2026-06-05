package online.entreprenly.platform.chatbot.application.queryservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllWhatsappSessionsQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetWhatsappSessionByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for WhatsApp session read operations.
 */
public interface WhatsappSessionQueryService {

    List<WhatsappSession> handle(GetAllWhatsappSessionsQuery query);

    Optional<WhatsappSession> handle(GetWhatsappSessionByIdQuery query);
}
