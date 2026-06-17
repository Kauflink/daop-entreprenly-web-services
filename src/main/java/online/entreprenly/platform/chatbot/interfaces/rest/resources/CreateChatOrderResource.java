package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Schema(name = "CreateChatOrderRequest", description = "Data required to create a chat order")
public record CreateChatOrderResource(
        @NotNull
        @Schema(description = "Identifier of the owning conversation", example = "1")
        Long conversationId,

        @Schema(description = "Human-readable order number (generated when absent)", nullable = true)
        String orderNumber,

        @NotEmpty
        @Schema(description = "Ordered line items")
        List<OrderItemResource> items,

        @Schema(description = "Delivery address", example = "Av. Los Alamos 234, Miraflores")
        String deliveryAddress,

        @Schema(description = "Payment method", example = "YAPE")
        String paymentMethod,

        @Schema(description = "Initial status (defaults to PENDING)", nullable = true)
        OrderStatus status
) {
}
