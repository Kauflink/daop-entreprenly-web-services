package online.entreprenly.platform.chatbot.application.internal.support;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public final class MessageTimeFormatter {

    private static final DateTimeFormatter HOUR_MINUTE =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneOffset.UTC);

    private MessageTimeFormatter() {
    }

    
    public static String toLabel(Instant instant) {
        return instant == null ? "" : HOUR_MINUTE.format(instant);
    }
}
