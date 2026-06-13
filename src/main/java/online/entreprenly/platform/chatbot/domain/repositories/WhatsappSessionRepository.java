package online.entreprenly.platform.chatbot.domain.repositories;

import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link WhatsappSession} aggregates.
 */
public interface WhatsappSessionRepository {

    List<WhatsappSession> findAll();

    List<WhatsappSession> findBySellerId(Long sellerId);

    Optional<WhatsappSession> findById(Long id);

    WhatsappSession save(WhatsappSession session);
}
