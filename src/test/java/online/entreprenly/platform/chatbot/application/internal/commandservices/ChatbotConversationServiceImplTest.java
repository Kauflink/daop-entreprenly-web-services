package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.chatbot.domain.model.aggregates.WhatsappSession;
import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.services.RuleBasedChatbotResponder;
import online.entreprenly.platform.chatbot.domain.services.RuleBasedProductReplyComposer;
import online.entreprenly.platform.chatbot.support.InMemoryChatMessageRepository;
import online.entreprenly.platform.chatbot.support.InMemoryConversationRepository;
import online.entreprenly.platform.chatbot.support.InMemoryWhatsappSessionRepository;
import online.entreprenly.platform.chatbot.support.RecordingEventPublisher;
import online.entreprenly.platform.chatbot.support.RecordingWhatsAppMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ChatbotConversationServiceImplTest {

    private InMemoryConversationRepository conversations;
    private InMemoryChatMessageRepository messages;
    private InMemoryWhatsappSessionRepository sessions;
    private RecordingWhatsAppMessagingService whatsApp;
    private ConversationCommandServiceImpl conversationCommandService;
    private ChatMessageCommandServiceImpl messageCommandService;

    /** Empty catalog by default, so the generic responder is used. */
    private final ProductCatalogService emptyCatalog = ownerEmail -> List.of();
    private final SellerEmailResolver noEmail = sellerId -> Optional.empty();

    @BeforeEach
    void setUp() {
        conversations = new InMemoryConversationRepository();
        messages = new InMemoryChatMessageRepository();
        sessions = new InMemoryWhatsappSessionRepository();
        var publisher = new RecordingEventPublisher();
        whatsApp = new RecordingWhatsAppMessagingService();
        conversationCommandService = new ConversationCommandServiceImpl(conversations, publisher);
        messageCommandService = new ChatMessageCommandServiceImpl(messages, conversations, publisher);
    }

    private ChatbotConversationServiceImpl service(ProductCatalogService catalog, SellerEmailResolver resolver) {
        return new ChatbotConversationServiceImpl(conversations, conversationCommandService, messageCommandService,
                sessions, new RuleBasedChatbotResponder(), whatsApp, catalog, resolver,
                new RuleBasedProductReplyComposer());
    }

    @Test
    @DisplayName("creates a conversation, stores client and bot messages, sends the reply and returns it")
    void handlesInboundMessageEndToEnd() {
        var service = service(emptyCatalog, noEmail);
        var command = new HandleInboundMessageCommand("+51 987 654 321", "Andrea", "Hola, quiero hacer un pedido");
        var result = service.handle(command);

        assertThat(result.isSuccess()).isTrue();
        var botReply = result.toOptional().orElseThrow();
        assertThat(botReply.getSender()).isEqualTo(MessageSender.BOT);
        assertThat(botReply.getContent()).isNotBlank();

        assertThat(conversations.findByClientPhone("+51 987 654 321")).isPresent();
        var convId = conversations.findByClientPhone("+51 987 654 321").orElseThrow().getId();
        var stored = messages.findByConversationId(convId);
        assertThat(stored).hasSize(2);
        assertThat(stored.get(0).getSender()).isEqualTo(MessageSender.CLIENT);
        assertThat(stored.get(1).getSender()).isEqualTo(MessageSender.BOT);

        assertThat(whatsApp.sent).hasSize(1);
        assertThat(whatsApp.sent.get(0).toPhone()).isEqualTo("+51 987 654 321");
    }

    @Test
    @DisplayName("reuses an existing conversation for a known phone number")
    void reusesExistingConversation() {
        var service = service(emptyCatalog, noEmail);
        service.handle(new HandleInboundMessageCommand("+51 900 000 000", "Cliente", "Hola"));
        service.handle(new HandleInboundMessageCommand("+51 900 000 000", "Cliente", "Buenas"));

        assertThat(conversations.findAll()).hasSize(1);
        var convId = conversations.findByClientPhone("+51 900 000 000").orElseThrow().getId();
        assertThat(messages.findByConversationId(convId)).hasSize(4);
    }

    @Test
    @DisplayName("answers with real product data when the seller has a catalog")
    void respondsWithRealProductData() {
        sessions.save(new WhatsappSession(1L, "+51 999 888 777", "Bodega", null));
        SellerEmailResolver resolver = sellerId -> sellerId == 1L ? Optional.of("seller@test.com") : Optional.empty();
        ProductCatalogService catalog = ownerEmail -> "seller@test.com".equals(ownerEmail)
                ? List.of(new CatalogProduct("Manzana", 4.50, true, 20.0))
                : List.of();

        var service = service(catalog, resolver);
        var result = service.handle(
                new HandleInboundMessageCommand("+51 987 000 111", "Cliente", "quiero 5 kilos de manzana"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.toOptional().orElseThrow().getContent()).contains("22.50");
    }
}
