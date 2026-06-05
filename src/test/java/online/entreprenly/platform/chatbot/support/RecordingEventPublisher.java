package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.events.ChatbotEventPublisher;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;

/**
 * Recording {@link ChatbotEventPublisher} that counts published events for assertions.
 */
public class RecordingEventPublisher implements ChatbotEventPublisher {

    public int messageEvents;
    public int conversationEvents;
    public int orderEvents;

    @Override
    public void publishMessageCreated(ChatMessage message) {
        messageEvents++;
    }

    @Override
    public void publishConversationChanged(Conversation conversation) {
        conversationEvents++;
    }

    @Override
    public void publishOrderChanged(ChatOrder order) {
        orderEvents++;
    }
}
