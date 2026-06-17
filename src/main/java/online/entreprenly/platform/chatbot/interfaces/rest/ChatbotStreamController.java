package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.infrastructure.realtime.sse.ChatbotSseService;
import online.entreprenly.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping(value = "/api/v1/chatbot/stream")
@Tag(name = "Chatbot - Realtime", description = "Server-Sent Events stream")
public class ChatbotStreamController {

    private final ChatbotSseService sseService;
    private final ChatbotSubscriptionGuard subscriptionGuard;
    private final BearerTokenService tokenService;

    public ChatbotStreamController(ChatbotSseService sseService,
                                   ChatbotSubscriptionGuard subscriptionGuard,
                                   BearerTokenService tokenService) {
        this.sseService = sseService;
        this.subscriptionGuard = subscriptionGuard;
        this.tokenService = tokenService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Subscribe to chatbot events",
            description = "Opens a Server-Sent Events stream that pushes message, conversation and order changes. "
                    + "Authenticates from a JWT passed as the 'token' query parameter, since EventSource cannot send headers.")
    public ResponseEntity<SseEmitter> stream(@RequestParam(value = "token", required = false) String token) {
        if (token == null || !tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var ownerEmail = tokenService.getUsernameFromToken(token);
        if (!subscriptionGuard.canAccessOwner(ownerEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(sseService.subscribe());
    }
}
