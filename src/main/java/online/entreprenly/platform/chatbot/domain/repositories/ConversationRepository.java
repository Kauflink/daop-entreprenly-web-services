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
     * Returns all conversations owned by the given seller.
     *
     * @param sellerId the seller identifier
     * @return the seller's conversations
     */
    List<Conversation> findAllBySellerId(Long sellerId);

    /**
     * Finds the most recent conversation held with a given client phone number
     * that belongs to a specific seller, used to route inbound WhatsApp messages.
     *
     * @param clientPhone the client's WhatsApp phone number
     * @param sellerId    the seller that owns the session receiving the message
     * @return the matching conversation, if any
     */
    Optional<Conversation> findByClientPhoneAndSellerId(String clientPhone, Long sellerId);

    Conversation save(Conversation conversation);
}
