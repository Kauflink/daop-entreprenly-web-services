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
import online.entreprenly.platform.chatbot.domain.model.commands.AttachReceiptCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.ConfirmChatOrderDeliveryCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatOrderCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateConversationCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundReceiptCommand;
import online.entreprenly.platform.chatbot.domain.model.commands.UpdateConversationCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderStatus;
import online.entreprenly.platform.chatbot.domain.repositories.ChatOrderRepository;
import online.entreprenly.platform.chatbot.domain.repositories.ConversationRepository;
import online.entreprenly.platform.chatbot.domain.services.ChatbotResponder;
import online.entreprenly.platform.chatbot.domain.services.ProductReplyComposer;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default {@link ChatbotConversationService} implementation.
 */
@Service
public class ChatbotConversationServiceImpl implements ChatbotConversationService {

    private static final long DEFAULT_SELLER_ID = 1L;

    private final ConversationRepository conversationRepository;
    private final ConversationCommandService conversationCommandService;
    private final ChatMessageCommandService messageCommandService;
    private final ChatbotResponder responder;
    private final WhatsAppMessagingService whatsAppMessagingService;
    private final ProductCatalogService productCatalogService;
    private final SellerEmailResolver sellerEmailResolver;
    private final ProductReplyComposer productReplyComposer;
    private final ConnectedSellerProvider connectedSellerProvider;
    private final ChatOrderCommandService chatOrderCommandService;
    private final ChatOrderRepository chatOrderRepository;

    /**
     * Lightweight per-conversation memory of the product last discussed, so a follow-up
     * such as "quisiera tres" binds to it. In-memory by design: it is a conversational
     * hint, not authoritative state, and is safe to lose on restart.
     */
    private final Map<Long, CatalogProduct> lastProductByConversation = new ConcurrentHashMap<>();

    public ChatbotConversationServiceImpl(ConversationRepository conversationRepository,
                                          ConversationCommandService conversationCommandService,
                                          ChatMessageCommandService messageCommandService,
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
        var replyText = composeReply(command.content(), conversation, command.ownerEmail());
        var outbound = new CreateChatMessageCommand(conversation.getId(), replyText,
                MessageSender.BOT, MessageType.TEXT, Instant.now());
        var outboundResult = messageCommandService.handle(outbound);
        if (outboundResult.isFailure()) {
            return outboundResult;
        }

        // The bridge delivers this reply itself (it sends the webhook's returned content),
        // so we must NOT also push it over /send here or the client receives it twice.
        return outboundResult;
    }

    @Override
    public Result<ChatMessage, ApplicationError> handle(HandleInboundReceiptCommand command) {
        if (command.fromPhone() == null || command.fromPhone().isBlank()) {
            return Result.failure(ApplicationError.validationError("fromPhone", "An origin phone is required"));
        }
        var sellerId = resolveSellerId(command.ownerEmail());
        var conversationOpt = conversationRepository.findByClientPhoneAndSellerId(command.fromPhone(), sellerId);
        if (conversationOpt.isEmpty()) {
            return Result.success(new ChatMessage(null, "Primero realiza tu pedido para validar tu pago.",
                    MessageSender.BOT, MessageType.TEXT, Instant.now()));
        }
        var conversation = conversationOpt.get();

        // Record the inbound receipt as an image message in the conversation.
        messageCommandService.handle(new CreateChatMessageCommand(conversation.getId(),
                "📷 Comprobante de pago", MessageSender.CLIENT, MessageType.IMAGE, Instant.now()));

        var order = chatOrderRepository.findByConversationId(conversation.getId()).stream()
                .filter(o -> o.getStatus() == OrderStatus.WAITING_PAYMENT)
                .findFirst();

        String replyText;
        if (order.isPresent()) {
            chatOrderCommandService.handle(new AttachReceiptCommand(order.get().getId(), command.image()));
            replyText = "¡Recibí tu comprobante! Lo estamos validando y te confirmamos en breve.";
        } else {
            replyText = "Recibí tu imagen, pero no tienes un pedido pendiente de pago. ¿Deseas hacer un pedido?";
        }

        var outbound = new CreateChatMessageCommand(conversation.getId(), replyText,
                MessageSender.BOT, MessageType.TEXT, Instant.now());
        var outboundResult = messageCommandService.handle(outbound);
        // The bridge delivers this reply itself (see handle(HandleInboundMessageCommand)).
        return outboundResult;
    }

    private Result<Conversation, ApplicationError> resolveConversation(HandleInboundMessageCommand command) {
        var sellerId = resolveSellerId(command.ownerEmail());
        var existing = conversationRepository.findByClientPhoneAndSellerId(command.fromPhone(), sellerId);
        if (existing.isPresent()) {
            return Result.success(existing.get());
        }
        var clientName = (command.clientName() == null || command.clientName().isBlank())
                ? command.fromPhone()
                : command.clientName();
        return conversationCommandService.handle(
                new CreateConversationCommand(sellerId, command.fromPhone(), clientName));
    }

    private Long resolveSellerId(String ownerEmail) {
        if (ownerEmail != null && !ownerEmail.isBlank()) {
            return sellerEmailResolver.resolveSellerId(ownerEmail).orElse(DEFAULT_SELLER_ID);
        }
        return DEFAULT_SELLER_ID;
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
    private String composeReply(String content, Conversation conversation, String ownerEmailFromBridge) {
        var catalog = resolveCatalog(conversation, ownerEmailFromBridge);
        var conversationId = conversation.getId();

        // 1) An explicit order in this very message, e.g. "quiero 3 coca cola".
        var orderLine = productReplyComposer.detectOrder(content, catalog);
        if (orderLine.isPresent()) {
            lastProductByConversation.remove(conversationId);
            return registerDraftOrder(conversation, orderLine.get());
        }

        // 2) The delivery address for a draft order that is awaiting it.
        var pending = findPendingOrder(conversationId);
        if (pending.isPresent() && looksLikeAddress(content)) {
            lastProductByConversation.remove(conversationId);
            return confirmDelivery(conversation, pending.get(), content.trim());
        }

        // 3) A follow-up quantity ("quisiera tres", "3") that continues the previous turn,
        //    bound to the product the bot was last talking about.
        var context = lastProductByConversation.get(conversationId);
        if (context != null) {
            var contextual = productReplyComposer.detectOrder(content, catalog, context);
            if (contextual.isPresent()) {
                lastProductByConversation.remove(conversationId);
                return registerDraftOrder(conversation, contextual.get());
            }
        }

        // 4) A product question (price/stock). Remember the product so the next message
        //    can be interpreted as a quantity for it.
        var productReply = productReplyComposer.compose(content, catalog);
        if (productReply.isPresent()) {
            productReplyComposer.matchProduct(content, catalog)
                    .ifPresent(product -> lastProductByConversation.put(conversationId, product));
            return productReply.get();
        }

        // 5) Generic conversational fallback.
        return responder.reply(content, conversation.getClientName());
    }

    /**
     * Resolves the seller's catalog. Prefers the email the receiving bridge sent with this
     * message (true multi-tenant: each seller's messages carry their own email); otherwise
     * falls back to the last connected bridge email or the conversation's seller id.
     */
    private List<CatalogProduct> resolveCatalog(Conversation conversation, String ownerEmailFromBridge) {
        var email = (ownerEmailFromBridge != null && !ownerEmailFromBridge.isBlank())
                ? Optional.of(ownerEmailFromBridge)
                : connectedSellerProvider.currentOwnerEmail()
                        .or(() -> sellerEmailResolver.resolveEmail(conversation.getSellerId()));
        return email.map(productCatalogService::findByOwner).orElseGet(List::of);
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
