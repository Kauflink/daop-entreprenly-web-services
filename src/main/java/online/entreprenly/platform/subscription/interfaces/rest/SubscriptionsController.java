package online.entreprenly.platform.subscription.interfaces.rest;

import online.entreprenly.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import online.entreprenly.platform.subscription.application.commandservices.SubscriptionCommandService;
import online.entreprenly.platform.subscription.application.queryservices.SubscriptionPlanQueryService;
import online.entreprenly.platform.subscription.application.queryservices.SubscriptionQueryService;
import online.entreprenly.platform.subscription.domain.model.commands.CancelSubscriptionCommand;
import online.entreprenly.platform.subscription.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetPaymentsBySubscriptionIdQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetSubscriptionByIdQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetSubscriptionPlanByIdQuery;
import online.entreprenly.platform.subscription.interfaces.rest.resources.AccessValidationResource;
import online.entreprenly.platform.subscription.interfaces.rest.resources.CreateSubscriptionResource;
import online.entreprenly.platform.subscription.interfaces.rest.resources.PaymentResource;
import online.entreprenly.platform.subscription.interfaces.rest.resources.ProcessSubscriptionPaymentResource;
import online.entreprenly.platform.subscription.interfaces.rest.resources.RenewSubscriptionResource;
import online.entreprenly.platform.subscription.interfaces.rest.resources.SubscriptionResource;
import online.entreprenly.platform.subscription.interfaces.rest.transform.CreateSubscriptionCommandFromResourceAssembler;
import online.entreprenly.platform.subscription.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import online.entreprenly.platform.subscription.interfaces.rest.transform.ProcessSubscriptionPaymentCommandFromResourceAssembler;
import online.entreprenly.platform.subscription.interfaces.rest.transform.RenewSubscriptionCommandFromResourceAssembler;
import online.entreprenly.platform.subscription.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * REST controller that exposes subscription resources.
 */
@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "User subscription and fake payment endpoints")
public class SubscriptionsController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;
    private final SubscriptionPlanQueryService subscriptionPlanQueryService;

    public SubscriptionsController(SubscriptionCommandService subscriptionCommandService,
                                   SubscriptionQueryService subscriptionQueryService,
                                   SubscriptionPlanQueryService subscriptionPlanQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.subscriptionPlanQueryService = subscriptionPlanQueryService;
    }

    @PostMapping
    @Operation(
            summary = "Create a subscription",
            description = "Creates a subscription and processes the first payment through the Fake Payment API.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created",
                    content = @Content(schema = @Schema(implementation = SubscriptionResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Plan not found"),
            @ApiResponse(responseCode = "409", description = "User already has an active subscription")
    })
    public ResponseEntity<?> createSubscription(@Valid @RequestBody CreateSubscriptionResource resource) {
        var command = CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping("/{subscriptionId}")
    @Operation(
            summary = "Get subscription by ID",
            description = "Retrieves a subscription by its unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription found",
                    content = @Content(schema = @Schema(implementation = SubscriptionResource.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<SubscriptionResource> getSubscriptionById(@PathVariable Long subscriptionId) {
        return subscriptionQueryService.handle(new GetSubscriptionByIdQuery(subscriptionId))
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get active subscription by user ID",
            description = "Retrieves the active non-expired subscription for a user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active subscription found",
                    content = @Content(schema = @Schema(implementation = SubscriptionResource.class))),
            @ApiResponse(responseCode = "404", description = "Active subscription not found")
    })
    public ResponseEntity<SubscriptionResource> getActiveSubscriptionByUserId(@RequestParam Long userId) {
        return subscriptionQueryService.handle(new GetActiveSubscriptionByUserIdQuery(userId))
                .filter(subscription -> subscription.isActiveAt(Instant.now()))
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{subscriptionId}/renewals")
    @Operation(
            summary = "Renew subscription",
            description = "Processes a fake payment and renews the subscription if the payment is approved.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription renewal processed",
                    content = @Content(schema = @Schema(implementation = SubscriptionResource.class))),
            @ApiResponse(responseCode = "404", description = "Subscription or plan not found")
    })
    public ResponseEntity<?> renewSubscription(@PathVariable Long subscriptionId,
                                               @Valid @RequestBody RenewSubscriptionResource resource) {
        var command = RenewSubscriptionCommandFromResourceAssembler.toCommandFromResource(subscriptionId, resource);
        var result = subscriptionCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/{subscriptionId}/cancellations")
    @Operation(
            summary = "Cancel subscription",
            description = "Cancels a subscription and records the cancellation timestamp.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription cancelled",
                    content = @Content(schema = @Schema(implementation = SubscriptionResource.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<?> cancelSubscription(@PathVariable Long subscriptionId) {
        var result = subscriptionCommandService.handle(new CancelSubscriptionCommand(subscriptionId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/{subscriptionId}/payments")
    @Operation(
            summary = "Process fake payment",
            description = "Processes a fake payment attempt for an existing subscription.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fake payment processed",
                    content = @Content(schema = @Schema(implementation = PaymentResource.class))),
            @ApiResponse(responseCode = "404", description = "Subscription or plan not found")
    })
    public ResponseEntity<?> processPayment(@PathVariable Long subscriptionId,
                                            @Valid @RequestBody ProcessSubscriptionPaymentResource resource) {
        var command = ProcessSubscriptionPaymentCommandFromResourceAssembler.toCommandFromResource(subscriptionId, resource);
        var result = subscriptionCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, PaymentResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping("/{subscriptionId}/payments")
    @Operation(
            summary = "List subscription payments",
            description = "Retrieves fake payment attempts for a subscription.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Payments found")
    public ResponseEntity<List<PaymentResource>> getPaymentsBySubscriptionId(@PathVariable Long subscriptionId) {
        var payments = subscriptionQueryService.handle(new GetPaymentsBySubscriptionIdQuery(subscriptionId)).stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/access")
    @Operation(
            summary = "Validate subscription access",
            description = "Checks whether a user has an active subscription and optional access to a plan feature.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Access validation completed")
    public ResponseEntity<AccessValidationResource> validateAccess(@RequestParam Long userId,
                                                                   @RequestParam(required = false) String feature) {
        var subscription = subscriptionQueryService.handle(new GetActiveSubscriptionByUserIdQuery(userId))
                .filter(item -> item.isActiveAt(Instant.now()));
        if (subscription.isEmpty()) {
            return ResponseEntity.ok(new AccessValidationResource(userId, feature, false, null, null, null));
        }
        var activeSubscription = subscription.get();
        var plan = subscriptionPlanQueryService.handle(new GetSubscriptionPlanByIdQuery(activeSubscription.getPlanId()));
        var hasAccess = plan.filter(item -> item.isActive() && item.hasFeature(feature)).isPresent();
        return ResponseEntity.ok(new AccessValidationResource(
                userId,
                feature,
                hasAccess,
                activeSubscription.getId(),
                activeSubscription.getPlanId(),
                activeSubscription.getStatus()));
    }
}
