package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.ChatMessageCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp.WhatsAppMessagingService;
import online.entreprenly.platform.chatbot.application.queryservices.ChatMessageQueryService;
import online.entreprenly.platform.chatbot.application.queryservices.ConversationQueryService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatMessagesQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatMessagesByConversationIdQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetConversationByIdQuery;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.ChatMessageResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateChatMessageResource;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.ChatMessageResourceFromEntityAssembler;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.CreateChatMessageCommandFromResourceAssembler;
import online.entreprenly.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing chat message resources.
 */
@RestController
@RequestMapping(value = "/api/v1/chat-messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chatbot - Chat Messages", description = "Conversation message endpoints")
public class ChatMessagesController {

    private final ChatMessageCommandService commandService;
    private final ChatMessageQueryService queryService;
    private final ConversationQueryService conversationQueryService;
    private final WhatsAppMessagingService whatsAppMessagingService;
    private final SellerEmailResolver sellerEmailResolver;
    private final ChatbotSubscriptionGuard subscriptionGuard;

    public ChatMessagesController(ChatMessageCommandService commandService,
                                  ChatMessageQueryService queryService,
                                  ConversationQueryService conversationQueryService,
                                  WhatsAppMessagingService whatsAppMessagingService,
                                  SellerEmailResolver sellerEmailResolver,
                                  ChatbotSubscriptionGuard subscriptionGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.conversationQueryService = conversationQueryService;
        this.whatsAppMessagingService = whatsAppMessagingService;
        this.sellerEmailResolver = sellerEmailResolver;
        this.subscriptionGuard = subscriptionGuard;
    }

    @GetMapping
    @Operation(summary = "List chat messages",
            description = "Retrieves messages for the authenticated seller, optionally filtered by conversation.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Messages found")
    public ResponseEntity<List<ChatMessageResource>> getMessages(
            @RequestParam(name = "conversationId", required = false) Long conversationId,
            Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var messages = (conversationId == null
                ? queryService.handle(new GetAllChatMessagesQuery(resolveSellerId(authentication)))
                : queryService.handle(new GetChatMessagesByConversationIdQuery(conversationId)))
                .stream()
                .map(ChatMessageResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(messages);
    }

    private Long resolveSellerId(Authentication authentication) {
        if (authentication == null) return 0L;
        return sellerEmailResolver.resolveSellerId(authentication.getName()).orElse(0L);
    }

    @PostMapping
    @Operation(summary = "Append a chat message", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message appended",
                    content = @Content(schema = @Schema(implementation = ChatMessageResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> createMessage(@Valid @RequestBody CreateChatMessageResource resource,
                                           Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var command = CreateChatMessageCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        result.toOptional().ifPresent(this::deliverBotMessageToClient);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ChatMessageResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    private void deliverBotMessageToClient(ChatMessage message) {
        if (message.getSender() != MessageSender.BOT || message.getType() != MessageType.TEXT) {
            return;
        }
        conversationQueryService.handle(new GetConversationByIdQuery(message.getConversationId()))
                .ifPresent(conversation -> {
                    var phone = conversation.getClientPhone();
                    if (phone == null || phone.isBlank()) return;
                    var ownerEmail = sellerEmailResolver.resolveEmail(conversation.getSellerId())
                            .orElse(null);
                    whatsAppMessagingService.sendText(ownerEmail, phone, message.getContent());
                });
    }
}