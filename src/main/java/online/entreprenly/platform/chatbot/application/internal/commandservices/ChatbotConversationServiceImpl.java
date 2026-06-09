package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.commandservices.ChatMessageCommandService;
import online.entreprenly.platform.chatbot.application.commandservices.ChatOrderCommandService;
import online.entreprenly.platform.chatbot.application.commandservices.ChatbotConversationService;
import online.entreprenly.platform.chatbot.application.commandservices.ConversationCommandService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ConnectedSellerProvider;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp.WhatsAppMessagingService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatMessage;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.commands.ConfirmChatOrderDeliveryCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateConversationCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateConversationCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import online.entreprenly.platform.chatbot.domain.repositories.ChatOrderRepository;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import online.entreprenly.platform.chatbot.domain.repositories.WhatsappSessionRepository;
import online.entreprenly.platform.chatbot.domain.services.ChatbotResponder;
import online.entreprenly.platform.chatbot.domain.services.ProductReplyComposer;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    private final ConnectedSellerProvider connectedSellerProvider;
    private final ChatOrderCommandService chatOrderCommandService;
    private final ChatOrderRepository chatOrderRepository;

    public ChatbotConversationServiceImpl(ConversationRepository conversationRepository,
                                          ConversationCommandService conversationCommandService,
                                          ChatMessageCommandService messageCommandService,
                                          WhatsappSessionRepository sessionRepository,
                                          ChatbotResponder responder,
                                          WhatsAppMessagingService whatsAppMessagingService,
                                          ProductCatalogService productCatalogService,
                                          SellerEmailResolver sellerEmailResolver,
                                          ProductReplyComposer productReplyComposer,
                                          ConnectedSellerProvider connectedSellerProvider,
                                          ChatOrderCommandService chatOrderCommandService,
                                          ChatOrderRepository chatOrderRepository) {
        this.conversationRepository = conversationRepository;
        this.conversationCommandService = conversationCommandService;
        this.messageCommandService = messageCommandService;
        this.sessionRepository = sessionRepository;
        this.responder = responder;
        this.whatsAppMessagingService = whatsAppMessagingService;
        this.productCatalogService = productCatalogService;
        this.sellerEmailResolver = sellerEmailResolver;
        this.productReplyComposer = productReplyComposer;
        this.connectedSellerProvider = connectedSellerProvider;
        this.chatOrderCommandService = chatOrderCommandService;
        this.chatOrderRepository = chatOrderRepository;
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
     * Builds the bot reply, driving the order flow:
     * <ol>
     *   <li>a concrete purchase ("quiero 3 coca cola") registers a draft order and asks the address;</li>
     *   <li>the next message, when a draft is pending, is taken as the delivery address and the order
     *       moves to awaiting payment;</li>
     *   <li>otherwise it answers catalogue questions or falls back to the generic responder.</li>
     * </ol>
     */
    private String composeReply(String content, Conversation conversation) {
        var catalog = resolveCatalog(conversation);

        var orderLine = productReplyComposer.detectOrder(content, catalog);
        if (orderLine.isPresent()) {
            return registerDraftOrder(conversation, orderLine.get());
        }

        var pending = findPendingOrder(conversation.getId());
        if (pending.isPresent() && looksLikeAddress(content)) {
            return confirmDelivery(conversation, pending.get(), content.trim());
        }

        return productReplyComposer.compose(content, catalog)
                .orElseGet(() -> responder.reply(content, conversation.getClientName()));
    }

    /** Resolves the seller's catalog, preferring the email reported by the connected bridge. */
    private List<CatalogProduct> resolveCatalog(Conversation conversation) {
        return connectedSellerProvider.currentOwnerEmail()
                .or(() -> sellerEmailResolver.resolveEmail(conversation.getSellerId()))
                .map(productCatalogService::findByOwner)
                .orElseGet(List::of);
    }

    private Optional<ChatOrder> findPendingOrder(Long conversationId) {
        return chatOrderRepository.findByConversationId(conversationId).stream()
                .filter(ChatOrder::isPending)
                .findFirst();
    }

    private String registerDraftOrder(Conversation conversation, OrderItem line) {
        var command = new CreateChatOrderCommand(conversation.getId(), null, List.of(line),
                null, "Por confirmar", OrderStatus.PENDING);
        var result = chatOrderCommandService.handle(command);
        var number = result.toOptional().map(ChatOrder::getOrderNumber).orElse("");
        double total = Math.round(line.unitPrice() * line.quantity() * 100.0) / 100.0;
        return "Anotado tu pedido %s: %d x %s = S/%.2f. ¿A qué dirección te lo enviamos?"
                .formatted(number, line.quantity(), line.productName(), total);
    }

    private String confirmDelivery(Conversation conversation, ChatOrder order, String address) {
        chatOrderCommandService.handle(new ConfirmChatOrderDeliveryCommand(order.getId(), address));
        conversationCommandService.handle(new UpdateConversationCommand(
                conversation.getId(), ConversationStatus.WAITING_PAYMENT, null, null));
        return "¡Listo! Registré tu pedido %s con entrega en \"%s\". Ahora envíame la captura de tu pago (Yape/Plin) para validarlo."
                .formatted(order.getOrderNumber(), address);
    }

    /** Heuristic: after asking for the address, a non-greeting reply is treated as the address. */
    private boolean looksLikeAddress(String content) {
        var text = content.trim().toLowerCase(java.util.Locale.ROOT);
        if (text.length() < 3) {
            return false;
        }
        return !text.matches("^(hola|buenas|buenos dias|buenas tardes|buenas noches|gracias|ok|si|no)\\.?$");
    }
}
