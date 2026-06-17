package online.entreprenly.platform.chatbot.interfaces.rest.transform;

import online.entreprenly.platform.chatbot.domain.model.commands.CreateWhatsappSessionCommand;
import online.entreprenly.platform.chatbot.interfaces.rest.resources.CreateWhatsappSessionResource;


public final class CreateWhatsappSessionCommandFromResourceAssembler {

    private CreateWhatsappSessionCommandFromResourceAssembler() {
    }

    public static CreateWhatsappSessionCommand toCommandFromResource(CreateWhatsappSessionResource resource) {
        return new CreateWhatsappSessionCommand(
                resource.sellerId(),
                resource.phone(),
                resource.businessName(),
                resource.qrCode());
    }
}
