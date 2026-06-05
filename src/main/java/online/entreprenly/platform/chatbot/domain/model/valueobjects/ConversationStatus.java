package online.entreprenly.platform.chatbot.domain.model.valueobjects;

/**
 * Lifecycle status of a {@code Conversation} between a seller's bot and a client.
 *
 * <p>The wire form matches the enum name (uppercase), as already expected by the
 * frontend conversation model.</p>
 */
public enum ConversationStatus {
    ACTIVE,
    WAITING_PAYMENT,
    COMPLETED,
    CLOSED
}
