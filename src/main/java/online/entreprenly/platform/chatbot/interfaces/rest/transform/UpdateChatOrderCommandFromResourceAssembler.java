package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.commands.UpdateChatOrderCommand;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.UpdateChatOrderResource;

/**
 * Assembler that translates an update resource into an update command.
 */
public final class UpdateChatOrderCommandFromResourceAssembler {

    private UpdateChatOrderCommandFromResourceAssembler() {
    }

    public static UpdateChatOrderCommand toCommandFromResource(Long orderId, UpdateChatOrderResource resource) {
        return new UpdateChatOrderCommand(orderId, resource.status(), resource.hasReceipt());
    }
}
