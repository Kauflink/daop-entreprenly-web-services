package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.commandservices.ConversationCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.events.ChatbotEventPublisher;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateConversationCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateConversationCommand;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;


@Service
public class ConversationCommandServiceImpl implements ConversationCommandService {

    private final ConversationRepository conversationRepository;
    private final ChatbotEventPublisher eventPublisher;

    public ConversationCommandServiceImpl(ConversationRepository conversationRepository,
                                          ChatbotEventPublisher eventPublisher) {
        this.conversationRepository = conversationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Conversation, ApplicationError> handle(CreateConversationCommand command) {
        if (command.sellerId() == null) {
            return Result.failure(ApplicationError.validationError("sellerId", "A seller is required"));
        }
        if (command.clientPhone() == null || command.clientPhone().isBlank()) {
            return Result.failure(ApplicationError.validationError("clientPhone", "A client phone is required"));
        }
        var conversation = new Conversation(command.sellerId(), command.clientPhone(), command.clientName());
        var saved = conversationRepository.save(conversation);
        eventPublisher.publishConversationChanged(saved);
        return Result.success(saved);
    }

    @Override
    public Result<Conversation, ApplicationError> handle(UpdateConversationCommand command) {
        return conversationRepository.findById(command.conversationId())
                .map(conversation -> {
                    conversation.changeStatus(command.status());
                    if (command.lastMessage() != null) {
                        conversation.registerLastMessage(command.lastMessage(), command.lastMessageTime());
                    }
                    var saved = conversationRepository.save(conversation);
                    eventPublisher.publishConversationChanged(saved);
                    return Result.<Conversation, ApplicationError>success(saved);
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("Conversation", String.valueOf(command.conversationId()))));
    }
}
