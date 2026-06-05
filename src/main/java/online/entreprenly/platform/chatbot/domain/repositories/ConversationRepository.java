package online.entreprenly.platform.chatbot.domain.repositories;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link Conversation} aggregates.
 */
public interface ConversationRepository {

    List<Conversation> findAll();

    Optional<Conversation> findById(Long id);

    /**
     * Finds the most recent conversation held with a given client phone number,
     * used to route inbound WhatsApp messages to an existing dialogue.
     *
     * @param clientPhone the client's WhatsApp phone number
     * @return the matching conversation, if any
     */
    Optional<Conversation> findByClientPhone(String clientPhone);

    Conversation save(Conversation conversation);
}
