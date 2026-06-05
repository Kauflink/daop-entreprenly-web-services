package online.entreprenly.platform.chatbot.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Content type of a {@code ChatMessage}. Text covers ordinary chat lines;
 * image covers payment receipts and other media references.
 */
public enum MessageType {
    TEXT("text"),
    IMAGE("image");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static MessageType fromValue(String value) {
        if (value == null) return TEXT;
        for (var type : values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown message type: " + value);
    }
}
