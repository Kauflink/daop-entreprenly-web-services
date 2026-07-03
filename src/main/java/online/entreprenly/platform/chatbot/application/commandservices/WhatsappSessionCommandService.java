package online.entreprenly.platform.chatbot.application.commandservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateWhatsappSessionCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.ReportBridgeConnectionCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateWhatsappSessionCommand;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;


public interface WhatsappSessionCommandService {

    Result<WhatsappSession, ApplicationError> handle(CreateWhatsappSessionCommand command);

    Result<WhatsappSession, ApplicationError> handle(UpdateWhatsappSessionCommand command);

    
    Result<WhatsappSession, ApplicationError> handle(ReportBridgeConnectionCommand command);
}
