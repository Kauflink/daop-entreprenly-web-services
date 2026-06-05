package online.entreprenly.platform.chatbot.application.internal.commandservices;

import online.entreprenly.platform.chatbot.domain.model.aggregates.Conversation;
import online.entreprenly.platform.chatbot.domain.model.commands.CreateChatMessageCommand;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
import online.entreprenly.platform.chatbot.support.InMemoryChatMessageRepository;
import online.entreprenly.platform.chatbot.support.InMemoryConversationRepository;
import online.entreprenly.platform.chatbot.support.RecordingEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ChatMessageCommandServiceImplTest {

    private InMemoryConversationRepository conversations;
    private InMemoryChatMessageRepository messages;
    private RecordingEventPublisher publisher;
    private ChatMessageCommandServiceImpl service;

    @BeforeEach
    void setUp() {
        conversations = new InMemoryConversationRepository();
        messages = new InMemoryChatMessageRepository();
        publisher = new RecordingEventPublisher();
        service = new ChatMessageCommandServiceImpl(messages, conversations, publisher);
    }

    @Test
    @DisplayName("persists the message, refreshes the conversation projection and publishes both changes")
    void persistsAndPublishes() {
        var conversation = conversations.save(new Conversation(1L, "+51 987 654 321", "Andrea"));

        var command = new CreateChatMessageCommand(conversation.getId(), "Hola, quiero un pedido",
                MessageSender.CLIENT, MessageType.TEXT, Instant.parse("2026-04-15T10:18:00Z"));
        var result = service.handle(command);

        assertThat(result.isSuccess()).isTrue();
        var saved = result.toOptional().orElseThrow();
        assertThat(saved.getId()).isNotNull();

        var refreshed = conversations.findById(conversation.getId()).orElseThrow();
        assertThat(refreshed.getLastMessage()).isEqualTo("Hola, quiero un pedido");
        assertThat(refreshed.getLastMessageTime()).isEqualTo("10:18");

        assertThat(publisher.messageEvents).isEqualTo(1);
        assertThat(publisher.conversationEvents).isEqualTo(1);
    }

    @Test
    @DisplayName("rejects an empty message")
    void rejectsEmptyContent() {
        var command = new CreateChatMessageCommand(1L, "   ", MessageSender.BOT, MessageType.TEXT, null);
        var result = service.handle(command);
        assertThat(result.isFailure()).isTrue();
    }
}
