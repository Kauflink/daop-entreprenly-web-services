package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.infrastructure.realtime.sse.ChatbotSseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Server-Sent Events stream for chatbot real-time updates.
 *
 * <p>Browser clients subscribe with an {@code EventSource}; the server pushes
 * {@code message}, {@code conversation} and {@code order} events as they happen, so the
 * dashboard reflects changes immediately without polling.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/chatbot/stream")
@Tag(name = "Chatbot - Realtime", description = "Server-Sent Events stream")
public class ChatbotStreamController {

    private final ChatbotSseService sseService;

    public ChatbotStreamController(ChatbotSseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Subscribe to chatbot events",
            description = "Opens a Server-Sent Events stream that pushes message, conversation and order changes.")
    public SseEmitter stream() {
        return sseService.subscribe();
    }
}
