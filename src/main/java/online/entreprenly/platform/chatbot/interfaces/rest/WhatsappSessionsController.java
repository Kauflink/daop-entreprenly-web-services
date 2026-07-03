package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.commandservices.WhatsappSessionCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.application.queryservices.WhatsappSessionQueryService;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllWhatsappSessionsQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetWhatsappSessionByIdQuery;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateWhatsappSessionResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.UpdateWhatsappSessionResource;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.WhatsappSessionResource;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.CreateWhatsappSessionCommandFromResourceAssembler;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.UpdateWhatsappSessionCommandFromResourceAssembler;
import online.entreprenly.platform.chatbot.interfaces.rest.transform.WhatsappSessionResourceFromEntityAssembler;
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


@RestController
@RequestMapping(value = "/api/v1/whatsapp-sessions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chatbot - WhatsApp Sessions", description = "WhatsApp channel session endpoints")
public class WhatsappSessionsController {

    private final WhatsappSessionCommandService commandService;
    private final WhatsappSessionQueryService queryService;
    private final SellerEmailResolver sellerEmailResolver;
    private final ChatbotSubscriptionGuard subscriptionGuard;

    public WhatsappSessionsController(WhatsappSessionCommandService commandService,
                                      WhatsappSessionQueryService queryService,
                                      SellerEmailResolver sellerEmailResolver,
                                      ChatbotSubscriptionGuard subscriptionGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.sellerEmailResolver = sellerEmailResolver;
        this.subscriptionGuard = subscriptionGuard;
    }

    @GetMapping
    @Operation(summary = "List WhatsApp sessions", description = "Retrieves WhatsApp sessions for the authenticated seller.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Sessions found")
    public ResponseEntity<List<WhatsappSessionResource>> getAllSessions(Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var sellerId = sellerEmailResolver.resolveSellerId(
                authentication != null ? authentication.getName() : "").orElse(0L);
        var sessions = queryService.handle(new GetAllWhatsappSessionsQuery(sellerId)).stream()
                .map(WhatsappSessionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{sessionId}")
    @Operation(summary = "Get WhatsApp session by ID",
            description = "Retrieves a single WhatsApp session by its unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session found",
                    content = @Content(schema = @Schema(implementation = WhatsappSessionResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found", content = @Content)
    })
    public ResponseEntity<WhatsappSessionResource> getSessionById(@PathVariable Long sessionId,
                                                                  Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return queryService.handle(new GetWhatsappSessionByIdQuery(sessionId))
                .map(WhatsappSessionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Register a WhatsApp session",
            description = "Registers a new WhatsApp channel session for a seller.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session registered",
                    content = @Content(schema = @Schema(implementation = WhatsappSessionResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateWhatsappSessionResource resource,
                                           Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var command = CreateWhatsappSessionCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, WhatsappSessionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{sessionId}")
    @Operation(summary = "Update a WhatsApp session status",
            description = "Updates the status of an existing WhatsApp session.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session updated",
                    content = @Content(schema = @Schema(implementation = WhatsappSessionResource.class))),
            @ApiResponse(responseCode = "404", description = "Session not found", content = @Content)
    })
    public ResponseEntity<?> updateSession(@PathVariable Long sessionId,
                                           @Valid @RequestBody UpdateWhatsappSessionResource resource,
                                           Authentication authentication) {
        if (!subscriptionGuard.canAccess(authentication)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var command = UpdateWhatsappSessionCommandFromResourceAssembler.toCommandFromResource(sessionId, resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, WhatsappSessionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}