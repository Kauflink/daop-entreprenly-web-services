package online.entreprenly.platform.chatbot.application.internal.outboundservices.events;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;

/**
 * Outbound port for pushing chatbot changes to subscribed clients in real time.
 *
 * <p>The application layer raises domain changes through this abstraction; the
 * concrete transport (Server-Sent Events) lives in the infrastructure layer so the
 * application remains unaware of how the push is delivered.</p>
 */
public interface ChatbotEventPublisher {

    /** Notifies subscribers that a new message was appended to a conversation. */
    void publishMessageCreated(ChatMessage message);

    /** Notifies subscribers that a conversation's state changed. */
    void publishConversationChanged(Conversation conversation);

    /** Notifies subscribers that an order's state changed. */
    void publishOrderChanged(ChatOrder order);
}
