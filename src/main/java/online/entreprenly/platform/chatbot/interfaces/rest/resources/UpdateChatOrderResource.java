package online.entreprenly.platform.chatbot.interfaces.rest.resources;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Request body to advance a chat order through its payment-review lifecycle.
 */
@Schema(name = "UpdateChatOrderRequest", description = "Data required to update a chat order")
public record UpdateChatOrderResource(
        @NotNull
        @Schema(description = "Desired status", example = "CONFIRMED")
        OrderStatus status,

        @Schema(description = "Whether a payment receipt is currently attached", example = "true")
        boolean hasReceipt
) {
}
