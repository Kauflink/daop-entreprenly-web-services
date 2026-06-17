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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class ChatbotSseService implements ChatbotEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ChatbotSseService.class);
    private static final long TIMEOUT_MILLIS = 30 * 60 * 1000L;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    
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
        
        
        Map<String, Object> slim = new LinkedHashMap<>();
        slim.put("id",              order.getId());
        slim.put("conversationId",  order.getConversationId());
        slim.put("orderNumber",     order.getOrderNumber());
        slim.put("items",           order.getItems());
        slim.put("total",           order.getTotal());
        slim.put("deliveryAddress", order.getDeliveryAddress());
        slim.put("paymentMethod",   order.getPaymentMethod());
        slim.put("status",          order.getStatus());
        slim.put("hasReceipt",      order.isHasReceipt());
        slim.put("rejectionCount",  order.getRejectionCount());
        slim.put("createdAt",       order.getCreatedAt());
        broadcast("order", slim);
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
