package online.entreprenly.platform.subscription.application.commandservices;

import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import online.entreprenly.platform.subscription.domain.model.aggregates.Payment;
import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.domain.model.commands.CancelSubscriptionCommand;
import online.entreprenly.platform.subscription.domain.model.commands.CreateSubscriptionCommand;
import online.entreprenly.platform.subscription.domain.model.commands.ProcessSubscriptionPaymentCommand;
import online.entreprenly.platform.subscription.domain.model.commands.RenewSubscriptionCommand;

/**
 * Subscription command service.
 */
public interface SubscriptionCommandService {
    Result<Subscription, ApplicationError> handle(CreateSubscriptionCommand command);
    Result<Subscription, ApplicationError> handle(RenewSubscriptionCommand command);
    Result<Subscription, ApplicationError> handle(CancelSubscriptionCommand command);
    Result<Payment, ApplicationError> handle(ProcessSubscriptionPaymentCommand command);
}
