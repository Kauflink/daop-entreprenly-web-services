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
    public boolean sendText(String ownerEmail, String toPhone, String content) {
        if (sendUrl == null || sendUrl.isBlank()) {
            log.debug("[whatsapp] bridge send-url not configured; skipping outbound to {}", toPhone);
            return false;
        }
        if (toPhone == null || toPhone.isBlank() || content == null || content.isBlank()) {
            return false;
        }
        if (ownerEmail == null || ownerEmail.isBlank()) {
            log.warn("[whatsapp] ownerEmail not resolved for outbound to {}; skipping", toPhone);
            return false;
        }
        var payload = "{\"email\":%s,\"phone\":%s,\"content\":%s}".formatted(
                jsonString(ownerEmail), jsonString(toPhone), jsonString(content));
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

    
    private static String jsonString(String value) {
        var escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
        return "\"" + escaped + "\"";
    }
}
