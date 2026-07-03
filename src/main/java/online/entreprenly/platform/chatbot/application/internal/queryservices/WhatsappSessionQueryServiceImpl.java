package online.entreprenly.platform.chatbot.application.internal.queryservices;

import online.entreprenly.platform.chatbot.application.queryservices.WhatsappSessionQueryService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.domain.model.queries.GetAllWhatsappSessionsQuery;
import online.entreprenly.platform.chatbot.domain.model.queries.GetWhatsappSessionByIdQuery;
import online.entreprenly.platform.chatbot.domain.repositories.WhatsappSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class WhatsappSessionQueryServiceImpl implements WhatsappSessionQueryService {

    private final WhatsappSessionRepository sessionRepository;

    public WhatsappSessionQueryServiceImpl(WhatsappSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<WhatsappSession> handle(GetAllWhatsappSessionsQuery query) {
        return sessionRepository.findBySellerId(query.sellerId());
    }

    @Override
    public Optional<WhatsappSession> handle(GetWhatsappSessionByIdQuery query) {
        return sessionRepository.findById(query.sessionId());
    }
}
