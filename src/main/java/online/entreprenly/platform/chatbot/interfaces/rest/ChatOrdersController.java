package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.ChatOrderCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.application.queryservices.ChatOrderQueryService;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllChatOrdersQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetChatOrderByIdQuery;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.ChatOrderResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateChatOrderResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.UpdateChatOrderResource;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.ChatOrderResourceFromEntityAssembler;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.CreateChatOrderCommandFromResourceAssembler;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.UpdateChatOrderCommandFromResourceAssembler;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing chat order resources.
 */
@RestController
@RequestMapping(value = "/api/v1/chat-orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chatbot - Chat Orders", description = "Conversation order endpoints")
public class ChatOrdersController {

    private final ChatOrderCommandService commandService;
    private final ChatOrderQueryService queryService;
    private final SellerEmailResolver sellerEmailResolver;
    private final ChatbotSubscriptionGuard subscriptionGuard;

    public ChatOrdersController(ChatOrderCommandService commandService,
                                ChatOrderQueryService queryService,
                                SellerEmailResolver sellerEmailResolver,
                                ChatbotSubscriptionGuard subscriptionGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.sellerEmailResolver = sellerEmailResolver;
        this.subscriptionGuard = subscriptionGuard;
    }

    @GetMapping
    @Operation(summary = "List chat orders", description = "Retrieves orders for the authenticated seller.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Orders found")
    public ResponseEntity<List<ChatOrderResource>> getAllOrders(Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var sellerId = sellerEmailResolver.resolveSellerId(
                authentication != null ? authentication.getName() : "").orElse(0L);
        var orders = queryService.handle(new GetAllChatOrdersQuery(sellerId)).stream()
                .map(ChatOrderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get chat order by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = ChatOrderResource.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    public ResponseEntity<ChatOrderResource> getOrderById(@PathVariable Long orderId,
                                                          Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return queryService.handle(new GetChatOrderByIdQuery(orderId))
                .map(ChatOrderResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a chat order", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created",
                    content = @Content(schema = @Schema(implementation = ChatOrderResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateChatOrderResource resource,
                                         Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var command = CreateChatOrderCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ChatOrderResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update a chat order", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated",
                    content = @Content(schema = @Schema(implementation = ChatOrderResource.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId,
                                         @Valid @RequestBody UpdateChatOrderResource resource,
                                         Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var command = UpdateChatOrderCommandFromResourceAssembler.toCommandFromResource(orderId, resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ChatOrderResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}