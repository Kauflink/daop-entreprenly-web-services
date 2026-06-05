package online.entreprenly.platform.chatbot.domain.model.valueobjects;

/**
 * Lifecycle status of a {@code ChatOrder} captured through the conversation.
 *
 * <p>The wire form matches the enum name (uppercase), as already expected by the
 * frontend order model.</p>
 */
public enum OrderStatus {
    PENDING,
    WAITING_PAYMENT,
    CONFIRMED,
    CANCELLED,
    BLOCKED
}
