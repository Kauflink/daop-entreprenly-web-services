package online.entreprenly.platform.chatbot.application.internal.outboundservices.events;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;


public interface ChatbotEventPublisher {

    
    void publishMessageCreated(ChatMessage message);

    
    void publishConversationChanged(Conversation conversation);

    
    void publishOrderChanged(ChatOrder order);
}
