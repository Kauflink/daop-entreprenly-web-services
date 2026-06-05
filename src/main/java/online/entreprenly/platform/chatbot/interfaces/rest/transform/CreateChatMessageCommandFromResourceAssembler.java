package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatMessageCommand;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateChatMessageResource;

/**
 * Assembler that translates a create resource into a create command.
 */
public final class CreateChatMessageCommandFromResourceAssembler {

    private CreateChatMessageCommandFromResourceAssembler() {
    }

    public static CreateChatMessageCommand toCommandFromResource(CreateChatMessageResource resource) {
        return new CreateChatMessageCommand(
                resource.conversationId(),
                resource.content(),
                resource.sender(),
                resource.type(),
                resource.sentAt());
    }
}
