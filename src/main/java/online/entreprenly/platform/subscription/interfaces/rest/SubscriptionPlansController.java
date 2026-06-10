package online.entreprenly.platform.subscription.interfaces.rest;

import online.entreprenly.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import online.entreprenly.platform.subscription.application.commandservices.SubscriptionPlanCommandService;
import online.entreprenly.platform.subscription.application.queryservices.SubscriptionPlanQueryService;
import online.entreprenly.platform.subscription.domain.model.queries.GetAllSubscriptionPlansQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetSubscriptionPlanByIdQuery;
import online.entreprenly.platform.subscription.interfaces.rest.resources.CreateSubscriptionPlanResource;
import online.entreprenly.platform.subscription.interfaces.rest.resources.SubscriptionPlanResource;
import online.entreprenly.platform.subscription.interfaces.rest.transform.CreateSubscriptionPlanCommandFromResourceAssembler;
import online.entreprenly.platform.subscription.interfaces.rest.transform.SubscriptionPlanResourceFromEntityAssembler;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller that exposes subscription plan resources.
 */
@RestController
@RequestMapping(value = "/api/v1/subscription-plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscription Plans", description = "Subscription plan management endpoints")
public class SubscriptionPlansController {

    private final SubscriptionPlanCommandService subscriptionPlanCommandService;
    private final SubscriptionPlanQueryService subscriptionPlanQueryService;

    public SubscriptionPlansController(SubscriptionPlanCommandService subscriptionPlanCommandService,
                                       SubscriptionPlanQueryService subscriptionPlanQueryService) {
        this.subscriptionPlanCommandService = subscriptionPlanCommandService;
        this.subscriptionPlanQueryService = subscriptionPlanQueryService;
    }

    @PostMapping
    @Operation(
            summary = "Create a subscription plan",
            description = "Creates a subscription plan that can be used by users to subscribe.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan created",
                    content = @Content(schema = @Schema(implementation = SubscriptionPlanResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "A plan with this name already exists")
    })
    public ResponseEntity<?> createSubscriptionPlan(@Valid @RequestBody CreateSubscriptionPlanResource resource) {
        var command = CreateSubscriptionPlanCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionPlanCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "List subscription plans",
            description = "Retrieves every subscription plan.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Plans found")
    public ResponseEntity<List<SubscriptionPlanResource>> getAllSubscriptionPlans() {
        var plans = subscriptionPlanQueryService.handle(new GetAllSubscriptionPlansQuery()).stream()
                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{planId}")
    @Operation(
            summary = "Get subscription plan by ID",
            description = "Retrieves a subscription plan by its unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan found",
                    content = @Content(schema = @Schema(implementation = SubscriptionPlanResource.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found", content = @Content)
    })
    public ResponseEntity<SubscriptionPlanResource> getSubscriptionPlanById(@PathVariable Long planId) {
        return subscriptionPlanQueryService.handle(new GetSubscriptionPlanByIdQuery(planId))
                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
