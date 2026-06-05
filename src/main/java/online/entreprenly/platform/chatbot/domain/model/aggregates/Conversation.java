package online.entreprenly.platform.chatbot.domain.model.aggregates;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Conversation aggregate root.
 *
 * <p>Represents an ongoing WhatsApp dialogue between a seller's bot and a single
 * client. It tracks the lightweight conversation-list projection ({@code lastMessage},
 * {@code lastMessageTime}) and the {@link ConversationStatus} lifecycle, owning the
 * transitions that close or complete the dialogue.</p>
 */
@Getter
public class Conversation extends AbstractDomainAggregateRoot<Conversation> {

    @Setter
    private Long id;
    private Long sellerId;
    private String clientPhone;
    private String clientName;
    private String lastMessage;
    private String lastMessageTime;
    private ConversationStatus status;
    private Instant createdAt;
    private Instant closedAt;

    public Conversation() {
    }

    public Conversation(Long sellerId, String clientPhone, String clientName) {
        this.sellerId = sellerId;
        this.clientPhone = clientPhone;
        this.clientName = clientName;
        this.status = ConversationStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.lastMessage = "";
        this.lastMessageTime = "";
    }

    /**
     * Records the latest message preview shown in the conversation list.
     *
     * @param content the message content
     * @param time    the display time label (e.g. {@code "10:18"})
     */
    public void registerLastMessage(String content, String time) {
        this.lastMessage = content;
        this.lastMessageTime = time;
    }

    /**
     * Applies a desired status transition, stamping {@code closedAt} when the
     * conversation reaches a terminal state.
     *
     * @param status the desired status (ignored when null)
     */
    public void changeStatus(ConversationStatus status) {
        if (status == null) return;
        this.status = status;
        this.closedAt = (status == ConversationStatus.CLOSED || status == ConversationStatus.COMPLETED)
                ? Instant.now()
                : null;
    }

    /**
     * Restores an aggregate from persistence.
     */
    public void restoreState(Long id, Long sellerId, String clientPhone, String clientName,
                             String lastMessage, String lastMessageTime, ConversationStatus status,
                             Instant createdAt, Instant closedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.clientPhone = clientPhone;
        this.clientName = clientName;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.status = status;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }
}
