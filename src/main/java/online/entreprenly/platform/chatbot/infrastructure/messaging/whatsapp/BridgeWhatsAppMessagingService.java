package online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp.WhatsAppMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * {@link WhatsAppMessagingService} adapter that delivers outbound messages through the
 * Node WhatsApp bridge ({@code POST /send}).
 *
 * <p>This is what actually reaches the client beyond the bridge's synchronous reply to an
 * inbound message: when the seller approves or rejects a payment from the app, or replies
 * manually, the resulting bot message is pushed here so the client is notified on WhatsApp.</p>
 *
 * <p>Marked {@link Primary} so it supersedes the logging stub. When
 * {@code chatbot.whatsapp.bridge-send-url} is not configured it degrades to a no-op (logging
 * only), so local runs without a live bridge keep working at zero cost.</p>
 */
@Service
@Primary
public class BridgeWhatsAppMessagingService implements WhatsAppMessagingService {

    private static final Logger log = LoggerFactory.getLogger(BridgeWhatsAppMessagingService.class);

    private final String sendUrl;
    private final String bridgeToken;
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public BridgeWhatsAppMessagingService(
            @Value("${chatbot.whatsapp.bridge-send-url:}") String sendUrl,
            @Value("${chatbot.whatsapp.bridge-token:entreprenly-bridge-secret}") String bridgeToken) {
        this.sendUrl = sendUrl;
        this.bridgeToken = bridgeToken;
    }

    @Override
    public boolean sendText(String toPhone, String content) {
        if (sendUrl == null || sendUrl.isBlank()) {
            log.debug("[whatsapp] bridge send-url not configured; skipping outbound to {}", toPhone);
            return false;
        }
        if (toPhone == null || toPhone.isBlank() || content == null || content.isBlank()) {
            return false;
        }
        var payload = "{\"phone\":%s,\"content\":%s}".formatted(jsonString(toPhone), jsonString(content));
        try {
            var request = HttpRequest.newBuilder(URI.create(sendUrl))
                    .header("Content-Type", "application/json")
                    .header("X-Bridge-Token", bridgeToken)
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                    .build();
            var response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                log.warn("[whatsapp] bridge /send returned {} for {}", response.statusCode(), toPhone);
                return false;
            }
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[whatsapp] interrupted delivering to {}: {}", toPhone, e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("[whatsapp] failed to deliver to {} via bridge: {}", toPhone, e.getMessage());
            return false;
        }
    }

    /** Minimal JSON string escaping for the small, controlled payload sent to the bridge. */
    private static String jsonString(String value) {
        var escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
        return "\"" + escaped + "\"";
    }
}
