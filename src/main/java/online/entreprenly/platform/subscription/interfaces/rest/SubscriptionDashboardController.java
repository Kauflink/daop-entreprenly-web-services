package online.entreprenly.platform.subscription.interfaces.rest;

import online.entreprenly.platform.iam.interfaces.acl.IamContextFacade;
import online.entreprenly.platform.subscription.application.internal.eventhandlers.SubscriptionCatalogReadyEventHandler;
import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionRepository;
import online.entreprenly.platform.subscription.interfaces.events.SubscriptionPlanChangedIntegrationEvent;
import online.entreprenly.platform.subscription.interfaces.rest.resources.SubscriptionDashboardResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Compatibility controller for the current Angular subscription dashboard.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscription Dashboard", description = "Frontend-compatible subscription dashboard endpoints")
public class SubscriptionDashboardController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneOffset.UTC);

    private final SubscriptionCatalogReadyEventHandler catalogReadyEventHandler;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final IamContextFacade iamContextFacade;
    private final Map<Long, SubscriptionDashboardResource.DashboardBillingSetupResource> billingSetupByUserId =
            new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public SubscriptionDashboardController(SubscriptionCatalogReadyEventHandler catalogReadyEventHandler,
                                           SubscriptionPlanRepository subscriptionPlanRepository,
                                           SubscriptionRepository subscriptionRepository,
                                           IamContextFacade iamContextFacade,
                                           ApplicationEventPublisher eventPublisher) {
        this.catalogReadyEventHandler = catalogReadyEventHandler;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.iamContextFacade = iamContextFacade;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Resolves the real user identifier from the authenticated principal (email), falling back to
     * the path identifier when no authentication is present (e.g. Swagger probes). The Angular
     * client sends a fixed path id, so the JWT is the source of truth for per-user persistence.
     */
    private Long resolveUserId(Authentication authentication, Long pathUserId) {
        if (authentication != null && authentication.getName() != null && !authentication.getName().isBlank()) {
            var resolved = iamContextFacade.fetchUserIdByEmail(authentication.getName());
            if (resolved != null && resolved != 0L) {
                return resolved;
            }
        }
        return pathUserId;
    }

    @GetMapping("/subscription-dashboard/{userId}")
    @Operation(
            summary = "Get subscription dashboard",
            description = "Returns the subscription dashboard shape currently consumed by Angular.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<SubscriptionDashboardResource> getDashboard(@PathVariable Long userId,
                                                                      Authentication authentication) {
        catalogReadyEventHandler.ensureDefaultPlans();
        return ResponseEntity.ok(toDashboard(resolveUserId(authentication, userId), false));
    }

    @PutMapping("/subscription-dashboard/{userId}")
    @Operation(
            summary = "Save subscription dashboard",
            description = "Compatibility endpoint for Angular local dashboard updates.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<SubscriptionDashboardResource> saveDashboard(@PathVariable Long userId,
                                                                       @RequestBody SubscriptionDashboardResource resource,
                                                                       Authentication authentication) {
        var resolvedUserId = resolveUserId(authentication, userId);
        if (resource != null && resource.billingSetup() != null) {
            billingSetupByUserId.put(resolvedUserId, resource.billingSetup());
        }
        applyPlanChange(resolvedUserId, resource);
        return ResponseEntity.ok(toDashboard(resolvedUserId, false));
    }

    /**
     * Persists the plan change implied by the dashboard the Angular client saves. Payment data is
     * intentionally not validated (simulated purchase): activating Plan Control immediately
     * activates a subscription with the requested billing period.
     */
    private void applyPlanChange(Long userId, SubscriptionDashboardResource resource) {
        if (resource == null || resource.currentPlan() == null) {
            return;
        }
        catalogReadyEventHandler.ensureDefaultPlans();
        var freePlan = subscriptionPlanRepository.findByCode(SubscriptionCatalogReadyEventHandler.FREE_PLAN_CODE)
                .orElseThrow();
        var controlPlan = subscriptionPlanRepository.findByCode(SubscriptionCatalogReadyEventHandler.CONTROL_PLAN_CODE)
                .orElseThrow();
        var targetCode = resource.currentPlan().id();
        var targetStatus = resource.currentPlan().status();
        var billingPeriod = "annual".equalsIgnoreCase(resource.defaultBillingCycle())
                ? BillingPeriod.YEARLY
                : BillingPeriod.MONTHLY;
        var activeSubscription = subscriptionRepository.findFirstByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .filter(subscription -> subscription.isActiveAt(Instant.now()));

        if (SubscriptionCatalogReadyEventHandler.CONTROL_PLAN_CODE.equals(targetCode)) {
            var currentControl = activeSubscription
                    .filter(subscription -> controlPlan.getId().equals(subscription.getPlanId()));
            if (currentControl.isPresent()) {
                var subscription = currentControl.get();
                if ("scheduled-cancellation".equals(targetStatus)) {
                    subscription.scheduleCancellation();
                } else {
                    subscription.resumeActive();
                }
                subscriptionRepository.save(subscription);
                publishPlanChanged(userId, controlPlan);
                return;
            }
            activeSubscription.ifPresent(subscription -> {
                subscription.cancel();
                subscriptionRepository.save(subscription);
            });
            var subscription = new Subscription(userId, controlPlan);
            subscription.activate(billingPeriod);
            subscriptionRepository.save(subscription);
            publishPlanChanged(userId, controlPlan);
            return;
        }

        if (SubscriptionCatalogReadyEventHandler.FREE_PLAN_CODE.equals(targetCode)) {
            var onFree = activeSubscription
                    .filter(subscription -> freePlan.getId().equals(subscription.getPlanId()))
                    .isPresent();
            activeSubscription
                    .filter(subscription -> !freePlan.getId().equals(subscription.getPlanId()))
                    .ifPresent(subscription -> {
                        subscription.cancel();
                        subscriptionRepository.save(subscription);
                    });
            if (!onFree) {
                var subscription = new Subscription(userId, freePlan);
                subscription.activateWithoutExpiration();
                subscriptionRepository.save(subscription);
            }
            publishPlanChanged(userId, freePlan);
        }
    }

    /**
     * Publishes the Subscription context's integration event so other bounded contexts (e.g.
     * Profile) can react to a plan change without coupling to the subscription domain.
     */
    private void publishPlanChanged(Long userId, SubscriptionPlan plan) {
        eventPublisher.publishEvent(new SubscriptionPlanChangedIntegrationEvent(userId, plan.getName()));
    }

    @GetMapping("/subscription-payment-confirmation/{userId}")
    @Operation(
            summary = "Get subscription payment confirmation dashboard",
            description = "Compatibility endpoint used by Angular before activating Plan Control.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<SubscriptionDashboardResource> getPaymentConfirmation(@PathVariable Long userId,
                                                                                Authentication authentication) {
        catalogReadyEventHandler.ensureDefaultPlans();
        return ResponseEntity.ok(toDashboard(resolveUserId(authentication, userId), true));
    }

    private SubscriptionDashboardResource toDashboard(Long userId, boolean forceControlPlan) {
        var freePlan = subscriptionPlanRepository.findByCode(SubscriptionCatalogReadyEventHandler.FREE_PLAN_CODE)
                .orElseThrow();
        var controlPlan = subscriptionPlanRepository.findByCode(SubscriptionCatalogReadyEventHandler.CONTROL_PLAN_CODE)
                .orElseThrow();
        var activeSubscription = subscriptionRepository.findFirstByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .filter(subscription -> subscription.isActiveAt(Instant.now()));
        var currentPlan = forceControlPlan
                ? toControlPlan(controlPlan, activeSubscription, false)
                : activeSubscription
                        .flatMap(subscription -> currentPlanFromSubscription(subscription, freePlan, controlPlan))
                        .orElseGet(() -> toFreePlan(freePlan));
        return new SubscriptionDashboardResource(
                userId,
                "monthly",
                currentPlan,
                toControlPlan(controlPlan, Optional.empty(), true),
                limitsForPlan(currentPlan),
                billingSetupByUserId.getOrDefault(userId, emptyBillingSetup()),
                defaultActivity(currentPlan));
    }

    private Optional<SubscriptionDashboardResource.DashboardPlanResource> currentPlanFromSubscription(
            Subscription subscription,
            SubscriptionPlan freePlan,
            SubscriptionPlan controlPlan) {
        if (freePlan.getId().equals(subscription.getPlanId())) {
            return Optional.of(toFreePlan(freePlan));
        }
        if (controlPlan.getId().equals(subscription.getPlanId())) {
            return Optional.of(toControlPlan(controlPlan, Optional.of(subscription), false));
        }
        return Optional.empty();
    }

    private SubscriptionDashboardResource.DashboardPlanResource toFreePlan(SubscriptionPlan plan) {
        return new SubscriptionDashboardResource.DashboardPlanResource(
                plan.getCode(),
                plan.getName(),
                "Plan gratuito asignado automaticamente al crear tu cuenta.",
                plan.getPrice().amount().doubleValue(),
                plan.getAnnualPrice().amount().doubleValue(),
                "free",
                "Plan Free",
                "Plan actual",
                false,
                null,
                null,
                List.of(
                        new SubscriptionDashboardResource.DashboardPlanFeatureResource("Inventario basico", true),
                        new SubscriptionDashboardResource.DashboardPlanFeatureResource("Movimientos manuales", true),
                        new SubscriptionDashboardResource.DashboardPlanFeatureResource("Chatbot no incluido", false)));
    }

    private SubscriptionDashboardResource.DashboardPlanResource toControlPlan(
            SubscriptionPlan plan,
            Optional<Subscription> subscription,
            boolean recommended) {
        var endDate = subscription.map(Subscription::getCurrentPeriodEnd).map(DATE_FORMATTER::format).orElse(null);
        var startDate = subscription.map(Subscription::getStartedAt).map(DATE_FORMATTER::format).orElse(null);
        var scheduledCancellation = subscription.map(Subscription::isCancellationScheduled).orElse(false);
        var shortDescription = describeControlPlan(endDate, scheduledCancellation);
        var status = scheduledCancellation ? "scheduled-cancellation" : "active";
        var statusLabel = recommended
                ? "Recomendado"
                : scheduledCancellation ? "Cancelacion programada" : "Plan Control activo";
        return new SubscriptionDashboardResource.DashboardPlanResource(
                plan.getCode(),
                plan.getName(),
                shortDescription,
                plan.getPrice().amount().doubleValue(),
                plan.getAnnualPrice().amount().doubleValue(),
                status,
                statusLabel,
                recommended ? "Recomendado" : "Plan actual",
                recommended,
                startDate,
                endDate,
                List.of(
                        new SubscriptionDashboardResource.DashboardPlanFeatureResource("Productos y lotes ilimitados", true),
                        new SubscriptionDashboardResource.DashboardPlanFeatureResource("Ventas, pedidos, caja y trazabilidad en un solo flujo.", true),
                        new SubscriptionDashboardResource.DashboardPlanFeatureResource("Chatbot de WhatsApp y alertas operativas incluidas.", true)));
    }

    private String describeControlPlan(String endDate, boolean scheduledCancellation) {
        if (endDate == null) {
            return "Opera sin restricciones con automatizaciones, alertas y trazabilidad completa.";
        }
        if (scheduledCancellation) {
            return "Tu plan sigue activo hasta el %s. No se renovara.".formatted(endDate);
        }
        return "Tu plan sigue activo hasta el %s. Se renovara automaticamente.".formatted(endDate);
    }

    private List<SubscriptionDashboardResource.DashboardLimitResource> limitsForPlan(
            SubscriptionDashboardResource.DashboardPlanResource currentPlan) {
        if (SubscriptionCatalogReadyEventHandler.FREE_PLAN_CODE.equals(currentPlan.id())) {
            return List.of(
                    new SubscriptionDashboardResource.DashboardLimitResource("products", "Productos", 0, 40),
                    new SubscriptionDashboardResource.DashboardLimitResource("active-batches", "Lotes activos", 0, 20),
                    new SubscriptionDashboardResource.DashboardLimitResource("users", "Usuarios", 1, 1));
        }
        return List.of(
                new SubscriptionDashboardResource.DashboardLimitResource("products", "Productos", 0, 0),
                new SubscriptionDashboardResource.DashboardLimitResource("active-batches", "Lotes activos", 0, 0),
                new SubscriptionDashboardResource.DashboardLimitResource("users", "Usuarios", 1, 5));
    }

    private SubscriptionDashboardResource.DashboardBillingSetupResource emptyBillingSetup() {
        return new SubscriptionDashboardResource.DashboardBillingSetupResource(
                "Metodo de pago",
                "Aun no hay tarjeta o medio de pago registrado.",
                "Agregar metodos de pago",
                "Datos de facturacion",
                "Completa RUC, razon social y correo de comprobantes.",
                "Completar datos",
                false,
                false,
                List.of(),
                null);
    }

    private List<SubscriptionDashboardResource.DashboardActivityResource> defaultActivity(
            SubscriptionDashboardResource.DashboardPlanResource currentPlan) {
        return List.of(
                new SubscriptionDashboardResource.DashboardActivityResource(
                        "created-account",
                        "Cuenta creada",
                        "Plan Free asignado automaticamente"),
                new SubscriptionDashboardResource.DashboardActivityResource(
                        "current-status",
                        "Estado actual",
                        "%s activo - S/ %.0f/mes".formatted(currentPlan.name(), currentPlan.monthlyPrice())),
                new SubscriptionDashboardResource.DashboardActivityResource(
                        "billing",
                        "Facturacion",
                        currentPlan.currentPeriodEndDate() == null
                                ? "Sin siguiente cobro"
                                : "Proxima renovacion: %s - pago mensual".formatted(currentPlan.currentPeriodEndDate())));
    }
}
