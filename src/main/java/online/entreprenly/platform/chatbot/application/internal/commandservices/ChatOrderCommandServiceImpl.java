package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.commandservices.ChatOrderCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.events.ChatbotEventPublisher;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.commands.AttachReceiptCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.ConfirmChatOrderDeliveryCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.repositories.ChatOrderRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Chat order command service implementation.
 *
 * <p>Order writes keep the aggregate authoritative over the payment-review flow
 * (receipt attachment, rejection counting and automatic blocking) and push every
 * change to subscribed clients in real time.</p>
 */
@Service
public class ChatOrderCommandServiceImpl implements ChatOrderCommandService {

    private final ChatOrderRepository orderRepository;
    private final ChatbotEventPublisher eventPublisher;

    public ChatOrderCommandServiceImpl(ChatOrderRepository orderRepository,
                                       ChatbotEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<ChatOrder, ApplicationError> handle(CreateChatOrderCommand command) {
        if (command.conversationId() == null) {
            return Result.failure(ApplicationError.validationError("conversationId", "A conversation is required"));
        }
        if (command.items() == null || command.items().isEmpty()) {
            return Result.failure(ApplicationError.validationError("items", "An order must contain at least one item"));
        }
        var orderNumber = (command.orderNumber() == null || command.orderNumber().isBlank())
                ? generateOrderNumber()
                : command.orderNumber();
        var order = new ChatOrder(command.conversationId(), orderNumber, command.items(),
                command.deliveryAddress(), command.paymentMethod(), command.status());
        var saved = orderRepository.save(order);
        eventPublisher.publishOrderChanged(saved);
        return Result.success(saved);
    }

    @Override
    public Result<ChatOrder, ApplicationError> handle(UpdateChatOrderCommand command) {
        return orderRepository.findById(command.orderId())
                .map(order -> {
                    applyTransition(order, command);
                    var saved = orderRepository.save(order);
                    eventPublisher.publishOrderChanged(saved);
                    return Result.<ChatOrder, ApplicationError>success(saved);
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("ChatOrder", String.valueOf(command.orderId()))));
    }

    @Override
    public Result<ChatOrder, ApplicationError> handle(ConfirmChatOrderDeliveryCommand command) {
        if (command.deliveryAddress() == null || command.deliveryAddress().isBlank()) {
            return Result.failure(ApplicationError.validationError("deliveryAddress", "A delivery address is required"));
        }
        return orderRepository.findById(command.orderId())
                .map(order -> {
                    order.confirmDelivery(command.deliveryAddress());
                    var saved = orderRepository.save(order);
                    eventPublisher.publishOrderChanged(saved);
                    return Result.<ChatOrder, ApplicationError>success(saved);
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("ChatOrder", String.valueOf(command.orderId()))));
    }

    @Override
    public Result<ChatOrder, ApplicationError> handle(AttachReceiptCommand command) {
        return orderRepository.findById(command.orderId())
                .map(order -> {
                    order.attachReceipt(command.receiptImage());
                    var saved = orderRepository.save(order);
                    eventPublisher.publishOrderChanged(saved);
                    return Result.<ChatOrder, ApplicationError>success(saved);
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("ChatOrder", String.valueOf(command.orderId()))));
    }

    private static void applyTransition(ChatOrder order, UpdateChatOrderCommand command) {
        switch (command.status()) {
            case CONFIRMED -> order.confirm();
            case WAITING_PAYMENT, BLOCKED -> {
                if (command.hasReceipt()) {
                    order.attachReceipt();
                } else {
                    order.rejectReceipt();
                }
            }
            case PENDING, CANCELLED -> order.changeStatus(command.status());
        }
    }

    private String generateOrderNumber() {
        return "#%04d".formatted(orderRepository.count() + 1);
    }
}
