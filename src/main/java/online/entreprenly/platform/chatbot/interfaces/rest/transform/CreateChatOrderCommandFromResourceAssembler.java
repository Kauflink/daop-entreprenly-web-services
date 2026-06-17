package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateChatOrderResource;

import java.util.List;


public final class CreateChatOrderCommandFromResourceAssembler {

    private CreateChatOrderCommandFromResourceAssembler() {
    }

    public static CreateChatOrderCommand toCommandFromResource(CreateChatOrderResource resource) {
        List<OrderItem> items = resource.items() == null ? List.of() : resource.items().stream()
                .map(item -> new OrderItem(item.productName(), item.quantity(), item.unitPrice()))
                .toList();
        return new CreateChatOrderCommand(
                resource.conversationId(),
                resource.orderNumber(),
                items,
                resource.deliveryAddress(),
                resource.paymentMethod(),
                resource.status());
    }
}
