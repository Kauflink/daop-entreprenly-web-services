package online.entreprenly.platform.chatbot.domain.model.aggregates;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChatOrderTest {

    private static ChatOrder sampleOrder() {
        return new ChatOrder(1L, "#0001",
                List.of(new OrderItem("Coca Cola 500ml", 3, 2.5), new OrderItem("Pan", 2, 0.5)),
                "Av. Los Alamos 234", "YAPE", OrderStatus.PENDING);
    }

    @Test
    @DisplayName("computes the total from its line subtotals")
    void computesTotalFromItems() {
        var order = sampleOrder();
        assertThat(order.getTotal()).isEqualTo(8.5);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.isHasReceipt()).isFalse();
    }

    @Test
    @DisplayName("confirms the order and marks the receipt as present")
    void confirmsOrder() {
        var order = sampleOrder();
        order.confirm();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(order.isHasReceipt()).isTrue();
    }

    @Test
    @DisplayName("blocks the order automatically after the second receipt rejection")
    void blocksAfterTwoRejections() {
        var order = sampleOrder();

        order.rejectReceipt();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.WAITING_PAYMENT);
        assertThat(order.getRejectionCount()).isEqualTo(1);
        assertThat(order.isBlocked()).isFalse();

        order.rejectReceipt();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.BLOCKED);
        assertThat(order.getRejectionCount()).isEqualTo(2);
        assertThat(order.isBlocked()).isTrue();
    }
}
