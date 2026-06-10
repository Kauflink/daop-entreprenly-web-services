package online.entreprenly.platform.chatbot.support;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp.WhatsAppMessagingService;

import java.util.ArrayList;
import java.util.List;

/**
 * Recording {@link WhatsAppMessagingService} that captures outbound messages.
 */
public class RecordingWhatsAppMessagingService implements WhatsAppMessagingService {

    public record Sent(String ownerEmail, String toPhone, String content) {
    }

    public final List<Sent> sent = new ArrayList<>();

    @Override
    public boolean sendText(String ownerEmail, String toPhone, String content) {
        sent.add(new Sent(ownerEmail, toPhone, content));
        return true;
    }
}
