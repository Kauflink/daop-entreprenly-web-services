package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.domain.model.commands.HandleInboundMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.services.RuleBasedChatbotResponder;
import online.entreprenly.platform.chatbot.support.InMemoryChatMessageRepository;
import online.entreprenly.platform.chatbot.support.InMemoryConversationRepository;
import online.entreprenly.platform.chatbot.support.InMemoryWhatsappSessionRepository;
import online.entreprenly.platform.chatbot.support.RecordingEventPublisher;
import online.entreprenly.platform.chatbot.support.RecordingWhatsAppMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatbotConversationServiceImplTest {

    private InMemoryConversationRepository conversations;
    private InMemoryChatMessageRepository messages;
    private RecordingWhatsAppMessagingService whatsApp;
    private ChatbotConversationServiceImpl service;

    @BeforeEach
    void setUp() {
        conversations = new InMemoryConversationRepository();
        messages = new InMemoryChatMessageRepository();
        var sessions = new InMemoryWhatsappSessionRepository();
        var publisher = new RecordingEventPublisher();
        whatsApp = new RecordingWhatsAppMessagingService();

        var conversationCommandService = new ConversationCommandServiceImpl(conversations, publisher);
        var messageCommandService = new ChatMessageCommandServiceImpl(messages, conversations, publisher);
        service = new ChatbotConversationServiceImpl(conversations, conversationCommandService,
                messageCommandService, sessions, new RuleBasedChatbotResponder(), whatsApp);
    }

    @Test
    @DisplayName("creates a conversation, stores client and bot messages, sends the reply and returns it")
    void handlesInboundMessageEndToEnd() {
        var command = new HandleInboundMessageCommand("+51 987 654 321", "Andrea", "Hola, quiero hacer un pedido");
        var result = service.handle(command);

        assertThat(result.isSuccess()).isTrue();
        var botReply = result.toOptional().orElseThrow();
        assertThat(botReply.getSender()).isEqualTo(MessageSender.BOT);
        assertThat(botReply.getContent()).isNotBlank();

        // A conversation was created for the new phone number.
        assertThat(conversations.findByClientPhone("+51 987 654 321")).isPresent();

        // Both the inbound client message and the bot reply were persisted.
        var convId = conversations.findByClientPhone("+51 987 654 321").orElseThrow().getId();
        var stored = messages.findByConversationId(convId);
        assertThat(stored).hasSize(2);
        assertThat(stored.get(0).getSender()).isEqualTo(MessageSender.CLIENT);
        assertThat(stored.get(1).getSender()).isEqualTo(MessageSender.BOT);

        // The reply was handed to the WhatsApp channel.
        assertThat(whatsApp.sent).hasSize(1);
        assertThat(whatsApp.sent.get(0).toPhone()).isEqualTo("+51 987 654 321");
    }

    @Test
    @DisplayName("reuses an existing conversation for a known phone number")
    void reusesExistingConversation() {
        var first = service.handle(
                new HandleInboundMessageCommand("+51 900 000 000", "Cliente", "Hola"));
        assertThat(first.isSuccess()).isTrue();

        service.handle(new HandleInboundMessageCommand("+51 900 000 000", "Cliente", "Precio del pan?"));

        assertThat(conversations.findAll()).hasSize(1);
        var convId = conversations.findByClientPhone("+51 900 000 000").orElseThrow().getId();
        // Two inbound + two bot replies.
        assertThat(messages.findByConversationId(convId)).hasSize(4);
    }
}
