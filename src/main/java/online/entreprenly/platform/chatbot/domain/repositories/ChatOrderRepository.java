package online.entreprenly.platform.chatbot.domain.repositories;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link ChatOrder} aggregates.
 */
public interface ChatOrderRepository {

    List<ChatOrder> findAll();

    Optional<ChatOrder> findById(Long id);

    List<ChatOrder> findByConversationId(Long conversationId);

    /**
     * Returns all orders whose conversation is in the given set of ids.
     * Used to scope orders to a single seller without adding a redundant
     * sellerId column to the chat_orders table.
     *
     * @param conversationIds the seller's conversation identifiers
     * @return the matching orders
     */
    List<ChatOrder> findByConversationIdIn(List<Long> conversationIds);

    long count();

    ChatOrder save(ChatOrder order);
}
