package online.entreprenly.platform.chatbot.domain.repositories;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;

import java.util.List;
import java.util.Optional;


public interface ChatOrderRepository {

    List<ChatOrder> findAll();

    Optional<ChatOrder> findById(Long id);

    List<ChatOrder> findByConversationId(Long conversationId);

    
    List<ChatOrder> findByConversationIdIn(List<Long> conversationIds);

    long count();

    ChatOrder save(ChatOrder order);
}
