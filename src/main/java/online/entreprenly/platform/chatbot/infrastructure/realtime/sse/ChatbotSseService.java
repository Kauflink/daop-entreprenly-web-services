package online.entreprenly.platform.chatbot.infrastructure.realtime.sse;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.events.ChatbotEventPublisher;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server-Sent Events broadcaster for chatbot changes.
 *
 * <p>Implements the {@link ChatbotEventPublisher} outbound port and keeps the set of
 * active browser subscriptions. Each domain change is pushed as a named SSE event
 * ({@code message}, {@code conversation}, {@code order}) so the frontend can update its
 * state immediately without polling.</p>
 */
@Service
public class ChatbotSseService implements ChatbotEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ChatbotSseService.class);
    private static final long TIMEOUT_MILLIS = 30 * 60 * 1000L;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * Registers a new subscriber and returns its emitter.
     *
     * @return the emitter bound to the subscription lifecycle
     */
    public SseEmitter subscribe() {
        var emitter = new SseEmitter(TIMEOUT_MILLIS);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(throwable -> emitters.remove(emitter));
        emitters.add(emitter);
        send(emitter, "connected", "ok");
        return emitter;
    }

    @Override
    public void publishMessageCreated(ChatMessage message) {
        broadcast("message", message);
    }

    @Override
    public void publishConversationChanged(Conversation conversation) {
        broadcast("conversation", conversation);
    }

    @Override
    public void publishOrderChanged(ChatOrder order) {
        broadcast("order", order);
    }

    private void broadcast(String eventName, Object payload) {
        for (var emitter : emitters) {
            send(emitter, eventName, payload);
        }
    }

    private void send(SseEmitter emitter, String eventName, Object payload) {
        try {
            emitter.send(SseEmitter.event().name(eventName).data(payload));
        } catch (IOException | IllegalStateException ex) {
            log.debug("Removing dead SSE emitter: {}", ex.getMessage());
            emitters.remove(emitter);
            emitter.completeWithError(ex);
        }
    }
}
