package online.entreprenly.platform.chatbot.domain.repositories;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;

import java.util.List;
import java.util.Optional;


public interface ConversationRepository {

    List<Conversation> findAll();

    Optional<Conversation> findById(Long id);

    
    List<Conversation> findAllBySellerId(Long sellerId);

    
    Optional<Conversation> findByClientPhoneAndSellerId(String clientPhone, Long sellerId);

    Conversation save(Conversation conversation);
}
