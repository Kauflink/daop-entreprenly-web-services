package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.commandservices.ChatMessageCommandService;
import online.entreprenly.platform.chatbot.application.commandservices.ChatbotConversationService;
import online.entreprenly.platform.chatbot.application.commandservices.ConversationCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp.WhatsAppMessagingService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateConversationCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import online.entreprenly.platform.chatbot.domain.repositories.WhatsappSessionRepository;
import online.entreprenly.platform.chatbot.domain.services.ChatbotResponder;
import online.entreprenly.platform.chatbot.domain.services.ProductReplyComposer;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Default {@link ChatbotConversationService} implementation.
 */
@Service
public class ChatbotConversationServiceImpl implements ChatbotConversationService {

    private static final long DEFAULT_SELLER_ID = 1L;

    private final ConversationRepository conversationRepository;
    private final ConversationCommandService conversationCommandService;
    private final ChatMessageCommandService messageCommandService;
    private final WhatsappSessionRepository sessionRepository;
    private final ChatbotResponder responder;
    private final WhatsAppMessagingService whatsAppMessagingService;
    private final ProductCatalogService productCatalogService;
    private final SellerEmailResolver sellerEmailResolver;
    private final ProductReplyComposer productReplyComposer;

    public ChatbotConversationServiceImpl(ConversationRepository conversationRepository,
                                          ConversationCommandService conversationCommandService,
                                          ChatMessageCommandService messageCommandService,
                                          WhatsappSessionRepository sessionRepository,
                                          ChatbotResponder responder,
                                          WhatsAppMessagingService whatsAppMessagingService,
                                          ProductCatalogService productCatalogService,
                                          SellerEmailResolver sellerEmailResolver,
                                          ProductReplyComposer productReplyComposer) {
        this.conversationRepository = conversationRepository;
        this.conversationCommandService = conversationCommandService;
        this.messageCommandService = messageCommandService;
        this.sessionRepository = sessionRepository;
        this.responder = responder;
        this.whatsAppMessagingService = whatsAppMessagingService;
        this.productCatalogService = productCatalogService;
        this.sellerEmailResolver = sellerEmailResolver;
        this.productReplyComposer = productReplyComposer;
    }

    @Override
    public Result<ChatMessage, ApplicationError> handle(HandleInboundMessageCommand command) {
        if (command.fromPhone() == null || command.fromPhone().isBlank()) {
            return Result.failure(ApplicationError.validationError("fromPhone", "An origin phone is required"));
        }
        if (command.content() == null || command.content().isBlank()) {
            return Result.failure(ApplicationError.validationError("content", "Message content cannot be empty"));
        }

        var conversationResult = resolveConversation(command);
        if (conversationResult.isFailure()) {
            return Result.failure(((Result.Failure<Conversation, ApplicationError>) conversationResult).error());
        }
        var conversation = ((Result.Success<Conversation, ApplicationError>) conversationResult).value();

        // Persist the inbound client message (refreshes the conversation projection and broadcasts).
        var inbound = new CreateChatMessageCommand(conversation.getId(), command.content(),
                MessageSender.CLIENT, MessageType.TEXT, Instant.now());
        var inboundResult = messageCommandService.handle(inbound);
        if (inboundResult.isFailure()) {
            return inboundResult;
        }

        // Produce and persist the automatic reply, grounded in the seller's real catalog
        // when the message is about products, and falling back to the generic responder.
        var replyText = composeReply(command.content(), conversation);
        var outbound = new CreateChatMessageCommand(conversation.getId(), replyText,
                MessageSender.BOT, MessageType.TEXT, Instant.now());
        var outboundResult = messageCommandService.handle(outbound);
        if (outboundResult.isFailure()) {
            return outboundResult;
        }

        // Deliver the reply through the WhatsApp channel (no-op stub by default).
        whatsAppMessagingService.sendText(command.fromPhone(), replyText);

        return outboundResult;
    }

    private Result<Conversation, ApplicationError> resolveConversation(HandleInboundMessageCommand command) {
        var existing = conversationRepository.findByClientPhone(command.fromPhone());
        if (existing.isPresent()) {
            return Result.success(existing.get());
        }
        var clientName = (command.clientName() == null || command.clientName().isBlank())
                ? command.fromPhone()
                : command.clientName();
        return conversationCommandService.handle(
                new CreateConversationCommand(resolveSellerId(), command.fromPhone(), clientName));
    }

    private Long resolveSellerId() {
        return sessionRepository.findAll().stream()
                .findFirst()
                .map(session -> session.getSellerId())
                .orElse(DEFAULT_SELLER_ID);
    }

    /**
     * Builds the bot reply: if the message is about products, it answers with the seller's
     * real catalog (price, stock, totals); otherwise it falls back to the generic responder.
     */
    private String composeReply(String content, Conversation conversation) {
        List<CatalogProduct> catalog = sellerEmailResolver.resolveEmail(conversation.getSellerId())
                .map(productCatalogService::findByOwner)
                .orElseGet(List::of);
        return productReplyComposer.compose(content, catalog)
                .orElseGet(() -> responder.reply(content, conversation.getClientName()));
    }
}
