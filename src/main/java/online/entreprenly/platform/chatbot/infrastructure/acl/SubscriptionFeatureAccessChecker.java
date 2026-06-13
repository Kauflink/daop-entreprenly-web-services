package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SubscriptionAccessChecker;
import online.entreprenly.platform.iam.application.queryservices.UserQueryService;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByEmailQuery;
import online.entreprenly.platform.subscription.application.queryservices.SubscriptionPlanQueryService;
import online.entreprenly.platform.subscription.application.queryservices.SubscriptionQueryService;
import online.entreprenly.platform.subscription.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import online.entreprenly.platform.subscription.domain.model.queries.GetSubscriptionPlanByIdQuery;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Anti-corruption adapter that validates chatbot access against the Subscription BC.
 */
@Service
public class SubscriptionFeatureAccessChecker implements SubscriptionAccessChecker {

    private final UserQueryService userQueryService;
    private final SubscriptionQueryService subscriptionQueryService;
    private final SubscriptionPlanQueryService subscriptionPlanQueryService;

    public SubscriptionFeatureAccessChecker(UserQueryService userQueryService,
                                            SubscriptionQueryService subscriptionQueryService,
                                            SubscriptionPlanQueryService subscriptionPlanQueryService) {
        this.userQueryService = userQueryService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.subscriptionPlanQueryService = subscriptionPlanQueryService;
    }

    @Override
    public boolean canUseChatbot(String ownerEmail) {
        if (ownerEmail == null || ownerEmail.isBlank()) {
            return false;
        }
        var now = Instant.now();
        return userQueryService.handle(new GetUserByEmailQuery(ownerEmail.trim()))
                .flatMap(user -> subscriptionQueryService.handle(new GetActiveSubscriptionByUserIdQuery(user.getId()))
                        .filter(subscription -> subscription.isActiveAt(now))
                        .flatMap(subscription -> subscriptionPlanQueryService.handle(
                                new GetSubscriptionPlanByIdQuery(subscription.getPlanId()))))
                .filter(plan -> plan.isActive() && plan.hasFeature(CHATBOT_FEATURE))
                .isPresent();
    }
}