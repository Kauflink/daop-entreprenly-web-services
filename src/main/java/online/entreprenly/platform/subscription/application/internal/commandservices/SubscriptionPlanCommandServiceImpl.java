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
        var code = command.code() == null || command.code().isBlank()
                ? command.name().trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "")
                : command.code().trim().toLowerCase();
        if (subscriptionPlanRepository.existsByCode(code)) {
            return Result.failure(ApplicationError.conflict("SubscriptionPlan", "A plan with this code already exists"));
        }
        if (subscriptionPlanRepository.existsByNameIgnoreCase(command.name().trim())) {
            return Result.failure(ApplicationError.conflict("SubscriptionPlan", "A plan with this name already exists"));
        }
        var price = new Money(command.amount(), command.currency());
        var annualPrice = new Money(command.annualAmount() == null ? command.amount() : command.annualAmount(), command.currency());
        var plan = new SubscriptionPlan(
                code,
                command.name().trim(),
                command.description(),
                price,
                annualPrice,
                command.billingPeriod() == null ? BillingPeriod.MONTHLY : command.billingPeriod(),
                command.features(),
                command.active() == null || command.active());
        return Result.success(subscriptionPlanRepository.save(plan));
    }
}
