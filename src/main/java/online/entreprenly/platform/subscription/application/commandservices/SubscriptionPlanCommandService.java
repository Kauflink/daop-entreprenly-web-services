package online.entreprenly.platform.subscription.application.commandservices;

import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.commands.CreateSubscriptionPlanCommand;

/**
 * Subscription plan command service.
 */
public interface SubscriptionPlanCommandService {
    Result<SubscriptionPlan, ApplicationError> handle(CreateSubscriptionPlanCommand command);
}
