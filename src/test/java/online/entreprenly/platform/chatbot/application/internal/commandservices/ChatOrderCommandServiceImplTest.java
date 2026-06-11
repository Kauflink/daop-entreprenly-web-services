package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.InventoryStockService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import online.entreprenly.platform.chatbot.support.EmptyConversationQueryService;
import online.entreprenly.platform.chatbot.support.InMemoryChatOrderRepository;
import online.entreprenly.platform.chatbot.support.RecordingEventPublisher;
import online.entreprenly.platform.chatbot.support.StubSellerEmailResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ChatOrderCommandServiceImplTest {

    private InMemoryChatOrderRepository orders;
    private RecordingEventPublisher publisher;
    private ChatOrderCommandServiceImpl service;

    /** No conversation/seller resolved, so stock deduction stays a no-op. */
    private final SellerEmailResolver noEmail = new StubSellerEmailResolver(sellerId -> Optional.empty());
    private final InventoryStockService noStock = (ownerEmail, items) -> { };

    @BeforeEach
    void setUp() {
        orders = new InMemoryChatOrderRepository();
        publisher = new RecordingEventPublisher();
        service = new ChatOrderCommandServiceImpl(orders, publisher,
                new EmptyConversationQueryService(), noEmail, noStock);
    }

    private Long createSampleOrder() {
        var create = new CreateChatOrderCommand(1L, null,
                List.of(new OrderItem("Coca Cola 500ml", 3, 2.5)), "Av. Los Alamos 234", "YAPE", OrderStatus.PENDING);
        var result = service.handle(create);
        assertThat(result.isSuccess()).isTrue();
        return result.toOptional().orElseThrow().getId();
    }

    @Test
    @DisplayName("generates an order number when none is provided and publishes the change")
    void generatesOrderNumber() {
        var create = new CreateChatOrderCommand(1L, null,
                List.of(new OrderItem("Pan", 2, 0.5)), "Av. Siempreviva", "CASH", null);
        var result = service.handle(create);

        assertThat(result.isSuccess()).isTrue();
        var order = result.toOptional().orElseThrow();
        assertThat(order.getOrderNumber()).isEqualTo("#0001");
        assertThat(order.getTotal()).isEqualTo(1.0);
        assertThat(publisher.orderEvents).isEqualTo(1);
    }

    @Test
    @DisplayName("confirms an order when the seller approves the receipt")
    void confirmsOrder() {
        var id = createSampleOrder();
        var result = service.handle(new UpdateChatOrderCommand(id, OrderStatus.CONFIRMED, true));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.toOptional().orElseThrow().getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    @DisplayName("blocks an order after two receipt rejections, staying authoritative over the count")
    void blocksAfterTwoRejections() {
        var id = createSampleOrder();

        service.handle(new UpdateChatOrderCommand(id, OrderStatus.WAITING_PAYMENT, false));
        var second = service.handle(new UpdateChatOrderCommand(id, OrderStatus.WAITING_PAYMENT, false));

        var order = second.toOptional().orElseThrow();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.BLOCKED);
        assertThat(order.getRejectionCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("fails when updating a missing order")
    void failsForMissingOrder() {
        var result = service.handle(new UpdateChatOrderCommand(999L, OrderStatus.CONFIRMED, true));
        assertThat(result.isFailure()).isTrue();
    }
}
