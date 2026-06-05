package online.entreprenly.platform.chatbot.domain.model.aggregates;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * ChatMessage aggregate root.
 *
 * <p>Represents a single message exchanged within a {@link Conversation}. Messages
 * are immutable records authored by the {@link MessageSender} (client, bot or system)
 * and carry their {@link MessageType} and the instant they were sent.</p>
 */
@Getter
public class ChatMessage extends AbstractDomainAggregateRoot<ChatMessage> {

    @Setter
    private Long id;
    private Long conversationId;
    private String content;
    private MessageSender sender;
    private MessageType type;
    private Instant sentAt;

    public ChatMessage() {
    }

    public ChatMessage(Long conversationId, String content, MessageSender sender, MessageType type, Instant sentAt) {
        this.conversationId = conversationId;
        this.content = content;
        this.sender = sender == null ? MessageSender.SYSTEM : sender;
        this.type = type == null ? MessageType.TEXT : type;
        this.sentAt = sentAt == null ? Instant.now() : sentAt;
    }

    /**
     * Restores an aggregate from persistence.
     */
    public void restoreState(Long id, Long conversationId, String content,
                             MessageSender sender, MessageType type, Instant sentAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.content = content;
        this.sender = sender;
        this.type = type;
        this.sentAt = sentAt;
    }
}
