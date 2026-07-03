package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.commands.CreateConversationCommand;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateConversationResource;


public final class CreateConversationCommandFromResourceAssembler {

    private CreateConversationCommandFromResourceAssembler() {
    }

    public static CreateConversationCommand toCommandFromResource(CreateConversationResource resource) {
        return new CreateConversationCommand(
                resource.sellerId(),
                resource.clientPhone(),
                resource.clientName());
    }
}
