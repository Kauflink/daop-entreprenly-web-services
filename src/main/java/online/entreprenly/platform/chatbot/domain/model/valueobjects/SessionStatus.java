package online.entreprenly.platform.chatbot.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Lifecycle status of a {@code WhatsappSession}.
 *
 * <p>The wire representation is lowercase to match the WhatsApp channel
 * vocabulary already consumed by the frontend, while the persisted name
 * remains uppercase through {@code @Enumerated(EnumType.STRING)}.</p>
 */
public enum SessionStatus {
    CONNECTED("connected"),
    DISCONNECTED("disconnected"),
    EXPIRED("expired");

    private final String value;

    SessionStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static SessionStatus fromValue(String value) {
        if (value == null) return null;
        for (var status : values()) {
            if (status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown session status: " + value);
    }
}
