package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Resource representing a chat order returned by the REST API.
 */
@Schema(name = "ChatOrderResponse", description = "An order captured through a conversation")
public record ChatOrderResource(
        @Schema(description = "Order unique identifier", example = "1")
        Long id,

        @Schema(description = "Identifier of the owning conversation", example = "1")
        Long conversationId,

        @Schema(description = "Human-readable order number", example = "#0042")
        String orderNumber,

        @Schema(description = "Ordered line items")
        List<OrderItemResource> items,

        @Schema(description = "Authoritative order total", example = "7.5")
        double total,

        @Schema(description = "Delivery address", example = "Av. Los Alamos 234, Miraflores")
        String deliveryAddress,

        @Schema(description = "Payment method", example = "YAPE")
        String paymentMethod,

        @Schema(description = "Order status", example = "WAITING_PAYMENT")
        OrderStatus status,

        @Schema(description = "Whether a payment receipt is attached", example = "true")
        boolean hasReceipt,

        @Schema(description = "Number of receipt rejections so far", example = "0")
        int rejectionCount,

        @Schema(description = "Instant the order was created")
        Instant createdAt,

        @Schema(description = "Payment receipt image as a data URL", nullable = true)
        String receiptImage
) {
}
