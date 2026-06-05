package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.commandservices.WhatsappSessionCommandService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateWhatsappSessionCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateWhatsappSessionCommand;
import online.entreprenly.platform.chatbot.domain.repositories.WhatsappSessionRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * WhatsApp session command service implementation.
 */
@Service
public class WhatsappSessionCommandServiceImpl implements WhatsappSessionCommandService {

    private final WhatsappSessionRepository sessionRepository;

    public WhatsappSessionCommandServiceImpl(WhatsappSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Result<WhatsappSession, ApplicationError> handle(CreateWhatsappSessionCommand command) {
        if (command.sellerId() == null) {
            return Result.failure(ApplicationError.validationError("sellerId", "A seller is required to create a session"));
        }
        if (command.phone() == null || command.phone().isBlank()) {
            return Result.failure(ApplicationError.validationError("phone", "A phone number is required"));
        }
        var session = new WhatsappSession(command.sellerId(), command.phone(), command.businessName(), command.qrCode());
        return Result.success(sessionRepository.save(session));
    }

    @Override
    public Result<WhatsappSession, ApplicationError> handle(UpdateWhatsappSessionCommand command) {
        return sessionRepository.findById(command.sessionId())
                .map(session -> {
                    session.changeStatus(command.status());
                    return Result.<WhatsappSession, ApplicationError>success(sessionRepository.save(session));
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("WhatsappSession", String.valueOf(command.sessionId()))));
    }
}
