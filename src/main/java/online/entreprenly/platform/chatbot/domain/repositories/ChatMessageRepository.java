package online.entreprenly.platform.chatbot.domain.repositories;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link ChatMessage} aggregates.
 */
public interface ChatMessageRepository {

    List<ChatMessage> findAll();

    Optional<ChatMessage> findById(Long id);

    /**
     * Returns the messages of a conversation ordered chronologically.
     *
     * @param conversationId the conversation identifier
     * @return the ordered messages
     */
    List<ChatMessage> findByConversationId(Long conversationId);

    /**
     * Returns all messages whose conversation is in the given set of ids.
     * Used to scope messages to a single seller.
     *
     * @param conversationIds the seller's conversation identifiers
     * @return the matching messages
     */
    List<ChatMessage> findByConversationIdIn(List<Long> conversationIds);

    ChatMessage save(ChatMessage message);
}
