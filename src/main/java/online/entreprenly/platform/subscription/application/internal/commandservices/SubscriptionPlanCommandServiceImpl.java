package online.entreprenly.platform.subscription.application.internal.commandservices;

import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import online.entreprenly.platform.subscription.application.commandservices.SubscriptionPlanCommandService;
import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.commands.CreateSubscriptionPlanCommand;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;

/**
 * Subscription plan command service implementation.
 */
@Service
public class SubscriptionPlanCommandServiceImpl implements SubscriptionPlanCommandService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanCommandServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public Result<SubscriptionPlan, ApplicationError> handle(CreateSubscriptionPlanCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            return Result.failure(ApplicationError.validationError("name", "A plan name is required"));
        }
        if (command.amount() == null) {
            return Result.failure(ApplicationError.validationError("amount", "A plan amount is required"));
        }
        if (subscriptionPlanRepository.existsByNameIgnoreCase(command.name().trim())) {
            return Result.failure(ApplicationError.conflict("SubscriptionPlan", "A plan with this name already exists"));
        }
        var price = new Money(command.amount(), command.currency());
        var plan = new SubscriptionPlan(
                command.name().trim(),
                command.description(),
                price,
                command.billingPeriod() == null ? BillingPeriod.MONTHLY : command.billingPeriod(),
                command.features(),
                command.active() == null || command.active());
        return Result.success(subscriptionPlanRepository.save(plan));
    }
}
