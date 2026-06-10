package online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp.WhatsAppMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Default {@link WhatsAppMessagingService} adapter.
 *
 * <p>This is a no-op logging stub: it records the outbound message instead of calling
 * any external provider, so the chatbot works end to end locally at zero cost and with
 * no credentials. When {@code chatbot.whatsapp.enabled=true} a real WhatsApp Cloud API
 * adapter is expected to supersede this bean; until then the flag only affects logging.</p>
 */
@Service
public class LoggingWhatsAppMessagingService implements WhatsAppMessagingService {

    private static final Logger log = LoggerFactory.getLogger(LoggingWhatsAppMessagingService.class);

    private final boolean enabled;

    public LoggingWhatsAppMessagingService(
            @Value("${chatbot.whatsapp.enabled:false}") boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean sendText(String ownerEmail, String toPhone, String content) {
        if (enabled) {
            log.info("[whatsapp] (enabled, no live adapter configured) would send to {} (owner={}): {}",
                    toPhone, ownerEmail, content);
        } else {
            log.debug("[whatsapp] (stub) outbound to {} (owner={}): {}", toPhone, ownerEmail, content);
        }
        return true;
    }
}
