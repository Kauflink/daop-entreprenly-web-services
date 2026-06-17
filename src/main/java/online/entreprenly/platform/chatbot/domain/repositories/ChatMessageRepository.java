package online.entreprenly.platform.chatbot.domain.repositories;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;

import java.util.List;
import java.util.Optional;


public interface ChatMessageRepository {

    List<ChatMessage> findAll();

    Optional<ChatMessage> findById(Long id);

    
    List<ChatMessage> findByConversationId(Long conversationId);

    
    List<ChatMessage> findByConversationIdIn(List<Long> conversationIds);

    ChatMessage save(ChatMessage message);
}
