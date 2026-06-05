package online.entreprenly.platform.subscription.application.internal.commandservices;

import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import online.entreprenly.platform.subscription.application.commandservices.SubscriptionCommandService;
import online.entreprenly.platform.subscription.application.internal.outboundservices.payments.PaymentGateway;
import online.entreprenly.platform.subscription.application.internal.outboundservices.payments.PaymentGatewayRequest;
import online.entreprenly.platform.subscription.domain.model.aggregates.Payment;
import online.entreprenly.platform.subscription.domain.model.aggregates.Subscription;
import online.entreprenly.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import online.entreprenly.platform.subscription.domain.model.commands.CancelSubscriptionCommand;
import online.entreprenly.platform.subscription.domain.model.commands.CreateSubscriptionCommand;
import online.entreprenly.platform.subscription.domain.model.commands.ProcessSubscriptionPaymentCommand;
import online.entreprenly.platform.subscription.domain.model.commands.RenewSubscriptionCommand;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentStatus;
import online.entreprenly.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import online.entreprenly.platform.subscription.domain.repositories.PaymentRepository;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import online.entreprenly.platform.subscription.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Subscription command service implementation.
 */
@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository,
                                          SubscriptionPlanRepository subscriptionPlanRepository,
                                          PaymentRepository paymentRepository,
                                          PaymentGateway paymentGateway) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public Result<Subscription, ApplicationError> handle(CreateSubscriptionCommand command) {
        if (command.userId() == null) {
            return Result.failure(ApplicationError.validationError("userId", "A user is required"));
        }
        var activeSubscription = subscriptionRepository
                .findFirstByUserIdAndStatus(command.userId(), SubscriptionStatus.ACTIVE);
        if (activeSubscription.filter(subscription -> subscription.isActiveAt(Instant.now())).isPresent()) {
            return Result.failure(ApplicationError.conflict("Subscription", "The user already has an active subscription"));
        }
        return subscriptionPlanRepository.findById(command.planId())
                .<Result<Subscription, ApplicationError>>map(plan -> {
                    if (!plan.isActive()) {
                        return Result.failure(ApplicationError.businessRuleViolation("Inactive plan", "Cannot subscribe to an inactive plan"));
                    }
                    var subscription = subscriptionRepository.save(new Subscription(command.userId(), plan));
                    var payment = processPayment(subscription, plan, command.paymentMethod(), command.cardToken(),
                            command.requestedPaymentStatus());
                    subscription.applyPayment(payment, plan.getBillingPeriod());
                    return Result.success(subscriptionRepository.save(subscription));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("SubscriptionPlan", String.valueOf(command.planId()))));
    }

    @Override
    public Result<Subscription, ApplicationError> handle(RenewSubscriptionCommand command) {
        return subscriptionRepository.findById(command.subscriptionId())
                .<Result<Subscription, ApplicationError>>map(subscription -> subscriptionPlanRepository.findById(subscription.getPlanId())
                        .<Result<Subscription, ApplicationError>>map(plan -> {
                            if (!plan.isActive()) {
                                return Result.failure(ApplicationError.businessRuleViolation("Inactive plan", "Cannot renew an inactive plan"));
                            }
                            var payment = processPayment(subscription, plan, command.paymentMethod(), command.cardToken(),
                                    command.requestedPaymentStatus());
                            subscription.applyPayment(payment, plan.getBillingPeriod());
                            return Result.success(subscriptionRepository.save(subscription));
                        })
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("SubscriptionPlan", String.valueOf(subscription.getPlanId())))))
                .orElseGet(() -> Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId()))));
    }

    @Override
    public Result<Subscription, ApplicationError> handle(CancelSubscriptionCommand command) {
        return subscriptionRepository.findById(command.subscriptionId())
                .<Result<Subscription, ApplicationError>>map(subscription -> {
                    subscription.cancel();
                    return Result.success(subscriptionRepository.save(subscription));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId()))));
    }

    @Override
    public Result<Payment, ApplicationError> handle(ProcessSubscriptionPaymentCommand command) {
        return subscriptionRepository.findById(command.subscriptionId())
                .<Result<Payment, ApplicationError>>map(subscription -> subscriptionPlanRepository.findById(subscription.getPlanId())
                        .<Result<Payment, ApplicationError>>map(plan -> {
                            var payment = processPayment(subscription, plan, command.paymentMethod(), command.cardToken(),
                                    command.requestedPaymentStatus());
                            subscription.applyPayment(payment, plan.getBillingPeriod());
                            subscriptionRepository.save(subscription);
                            return Result.success(payment);
                        })
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("SubscriptionPlan", String.valueOf(subscription.getPlanId())))))
                .orElseGet(() -> Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId()))));
    }

    private Payment processPayment(Subscription subscription, SubscriptionPlan plan, String paymentMethod,
                                   String cardToken, PaymentStatus requestedStatus) {
        var request = new PaymentGatewayRequest(
                subscription.getId(),
                subscription.getUserId(),
                plan.getId(),
                plan.getPrice(),
                paymentMethod,
                cardToken,
                requestedStatus);
        var response = paymentGateway.process(request);
        var payment = new Payment(
                subscription.getId(),
                subscription.getUserId(),
                plan.getId(),
                plan.getPrice(),
                paymentMethod,
                response.status(),
                response.transactionId(),
                response.message(),
                requestedStatus);
        return paymentRepository.save(payment);
    }
}
