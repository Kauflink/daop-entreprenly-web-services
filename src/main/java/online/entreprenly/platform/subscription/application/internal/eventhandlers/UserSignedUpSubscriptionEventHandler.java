package online.entreprenly.platform.subscription.application.internal.eventhandlers;

import online.entreprenly.platform.iam.interfaces.events.UserSignedUpIntegrationEvent;
import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Assigns the free subscription when an IAM account is created.
 */
@Component
@Slf4j
public class UserSignedUpSubscriptionEventHandler {

    private final SubscriptionCatalogReadyEventHandler catalogReadyEventHandler;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    public UserSignedUpSubscriptionEventHandler(SubscriptionCatalogReadyEventHandler catalogReadyEventHandler,
                                                SubscriptionPlanRepository subscriptionPlanRepository,
                                                SubscriptionRepository subscriptionRepository) {
        this.catalogReadyEventHandler = catalogReadyEventHandler;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @EventListener
    public void on(UserSignedUpIntegrationEvent event) {
        catalogReadyEventHandler.ensureDefaultPlans();
        var existingActive = subscriptionRepository.findFirstByUserIdAndStatus(event.userId(), SubscriptionStatus.ACTIVE)
                .filter(subscription -> subscription.isActiveAt(Instant.now()));
        if (existingActive.isPresent()) {
            log.warn("User {} already has an active subscription", event.userId());
            return;
        }
        subscriptionPlanRepository.findByCode(SubscriptionCatalogReadyEventHandler.FREE_PLAN_CODE)
                .ifPresentOrElse(plan -> {
                    var subscription = new Subscription(event.userId(), plan);
                    subscription.activateWithoutExpiration();
                    subscriptionRepository.save(subscription);
                    log.info("Free subscription created for user {}", event.userId());
                }, () -> log.warn("Could not create free subscription for user {}: free plan not found", event.userId()));
    }
}
