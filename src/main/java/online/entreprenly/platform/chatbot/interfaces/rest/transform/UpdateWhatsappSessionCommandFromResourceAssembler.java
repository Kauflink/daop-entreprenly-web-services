package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.commands.UpdateWhatsappSessionCommand;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.UpdateWhatsappSessionResource;

/**
 * Assembler that translates an update resource into an update command.
 */
public final class UpdateWhatsappSessionCommandFromResourceAssembler {

    private UpdateWhatsappSessionCommandFromResourceAssembler() {
    }

    public static UpdateWhatsappSessionCommand toCommandFromResource(Long sessionId,
                                                                     UpdateWhatsappSessionResource resource) {
        return new UpdateWhatsappSessionCommand(sessionId, resource.status());
    }
}
