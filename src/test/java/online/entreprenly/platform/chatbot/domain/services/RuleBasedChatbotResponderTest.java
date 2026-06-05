package online.entreprenly.platform.chatbot.domain.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuleBasedChatbotResponderTest {

    private final ChatbotResponder responder = new RuleBasedChatbotResponder();

    @Test
    @DisplayName("greets the client by name")
    void greetsClient() {
        var reply = responder.reply("Hola, buenas tardes", "Andrea");
        assertThat(reply).contains("Andrea").containsIgnoringCase("bienvenido");
    }

    @Test
    @DisplayName("acknowledges an order intent")
    void acknowledgesOrder() {
        var reply = responder.reply("Quiero comprar 3 gaseosas", null);
        assertThat(reply).containsIgnoringCase("pedido");
    }

    @Test
    @DisplayName("falls back to a generic reply for unknown intents")
    void fallsBack() {
        var reply = responder.reply("asdfghjkl", null);
        assertThat(reply).isNotBlank();
    }
}
