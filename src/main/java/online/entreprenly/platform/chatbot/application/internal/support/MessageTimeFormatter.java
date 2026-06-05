package online.entreprenly.platform.chatbot.application.internal.support;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Formats message instants into the short {@code HH:mm} label shown in the
 * conversation list projection.
 */
public final class MessageTimeFormatter {

    private static final DateTimeFormatter HOUR_MINUTE =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneOffset.UTC);

    private MessageTimeFormatter() {
    }

    /**
     * @param instant the message instant (nullable)
     * @return the {@code HH:mm} label, or an empty string when the instant is null
     */
    public static String toLabel(Instant instant) {
        return instant == null ? "" : HOUR_MINUTE.format(instant);
    }
}
