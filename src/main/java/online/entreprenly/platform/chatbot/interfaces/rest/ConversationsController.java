package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.ConversationCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.application.queryservices.ConversationQueryService;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllConversationsQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetConversationByIdQuery;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.ConversationResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateConversationResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.UpdateConversationResource;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.ConversationResourceFromEntityAssembler;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.CreateConversationCommandFromResourceAssembler;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.UpdateConversationCommandFromResourceAssembler;
import online.entreprenly.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing conversation resources.
 */
@RestController
@RequestMapping(value = "/api/v1/conversations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chatbot - Conversations", description = "WhatsApp conversation endpoints")
public class ConversationsController {

    private final ConversationCommandService commandService;
    private final ConversationQueryService queryService;
    private final ChatbotSubscriptionGuard subscriptionGuard;

    public ConversationsController(ConversationCommandService commandService,
                                   ConversationQueryService queryService,
                                   ChatbotSubscriptionGuard subscriptionGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.subscriptionGuard = subscriptionGuard;
    private final SellerEmailResolver sellerEmailResolver;

    public ConversationsController(ConversationCommandService commandService,
                                   ConversationQueryService queryService,
                                   SellerEmailResolver sellerEmailResolver) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.sellerEmailResolver = sellerEmailResolver;
    }

    @GetMapping
    @Operation(summary = "List conversations", description = "Retrieves conversations for the authenticated seller.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Conversations found")
    public ResponseEntity<List<ConversationResource>> getAllConversations(Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var sellerId = resolveSellerId(authentication);
        var conversations = queryService.handle(new GetAllConversationsQuery(sellerId)).stream()
                .map(ConversationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}")
    @Operation(summary = "Get conversation by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversation found",
                    content = @Content(schema = @Schema(implementation = ConversationResource.class))),
            @ApiResponse(responseCode = "404", description = "Conversation not found", content = @Content)
    })
    public ResponseEntity<ConversationResource> getConversationById(@PathVariable Long conversationId,
                                                                      Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return queryService.handle(new GetConversationByIdQuery(conversationId))
                .map(ConversationResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Start a conversation", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conversation created",
                    content = @Content(schema = @Schema(implementation = ConversationResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> createConversation(@Valid @RequestBody CreateConversationResource resource,
                                                Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var command = CreateConversationCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ConversationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    private Long resolveSellerId(Authentication authentication) {
        if (authentication == null) return 0L;
        return sellerEmailResolver.resolveSellerId(authentication.getName()).orElse(0L);
    }

    @PutMapping("/{conversationId}")
    @Operation(summary = "Update a conversation", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversation updated",
                    content = @Content(schema = @Schema(implementation = ConversationResource.class))),
            @ApiResponse(responseCode = "404", description = "Conversation not found", content = @Content)
    })
    public ResponseEntity<?> updateConversation(@PathVariable Long conversationId,
                                                @Valid @RequestBody UpdateConversationResource resource,
                                                Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var command = UpdateConversationCommandFromResourceAssembler.toCommandFromResource(conversationId, resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ConversationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
