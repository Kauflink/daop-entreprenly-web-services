package online.entreprenly.platform.chatbot.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum MessageSender {
    CLIENT("client"),
    BOT("bot"),
    SYSTEM("system");

    private final String value;

    MessageSender(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static MessageSender fromValue(String value) {
        if (value == null) return null;
        for (var sender : values()) {
            if (sender.value.equalsIgnoreCase(value) || sender.name().equalsIgnoreCase(value)) {
                return sender;
            }
        }
        throw new IllegalArgumentException("Unknown message sender: " + value);
    }
}
