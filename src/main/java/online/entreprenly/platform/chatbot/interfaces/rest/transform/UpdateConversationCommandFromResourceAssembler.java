package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.commands.UpdateConversationCommand;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.UpdateConversationResource;


public final class UpdateConversationCommandFromResourceAssembler {

    private UpdateConversationCommandFromResourceAssembler() {
    }

    public static UpdateConversationCommand toCommandFromResource(Long conversationId,
                                                                  UpdateConversationResource resource) {
        return new UpdateConversationCommand(
                conversationId,
                resource.status(),
                resource.lastMessage(),
                resource.lastMessageTime());
    }
}
