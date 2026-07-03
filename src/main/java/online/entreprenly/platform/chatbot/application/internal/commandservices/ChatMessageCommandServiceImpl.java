package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.commandservices.ChatMessageCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.events.ChatbotEventPublisher;
import online.entreprenly.platform.chatbot.application.internal.support.MessageTimeFormatter;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import online.entreprenly.platform.chatbot.domain.repositories.ChatMessageRepository;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;


@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatMessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ChatbotEventPublisher eventPublisher;

    public ChatMessageCommandServiceImpl(ChatMessageRepository messageRepository,
                                         ConversationRepository conversationRepository,
                                         ChatbotEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<ChatMessage, ApplicationError> handle(CreateChatMessageCommand command) {
        if (command.conversationId() == null) {
            return Result.failure(ApplicationError.validationError("conversationId", "A conversation is required"));
        }
        if (command.content() == null || command.content().isBlank()) {
            return Result.failure(ApplicationError.validationError("content", "Message content cannot be empty"));
        }
        var message = new ChatMessage(command.conversationId(), command.content(),
                command.sender(), command.type(), command.sentAt());
        var saved = messageRepository.save(message);

        conversationRepository.findById(saved.getConversationId()).ifPresent(conversation -> {
            
            var preview = saved.getType() == MessageType.IMAGE ? "📷 Comprobante" : saved.getContent();
            conversation.registerLastMessage(preview, MessageTimeFormatter.toLabel(saved.getSentAt()));
            var updatedConversation = conversationRepository.save(conversation);
            eventPublisher.publishConversationChanged(updatedConversation);
        });

        eventPublisher.publishMessageCreated(saved);
        return Result.success(saved);
    }
}
