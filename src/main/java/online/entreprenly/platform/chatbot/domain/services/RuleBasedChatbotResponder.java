package online.entreprenly.platform.chatbot.domain.services;

import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Rule-based implementation of {@link ChatbotResponder}.
 *
 * <p>Matches a small set of intents (greeting, catalogue, pricing/stock, ordering and
 * payment) against the incoming text and returns a Spanish reply. This keeps the
 * conversation flowing automatically without depending on any paid NLP service.</p>
 */
@Service
public class RuleBasedChatbotResponder implements ChatbotResponder {

    @Override
    public String reply(String incomingContent, String clientName) {
        var name = (clientName == null || clientName.isBlank()) ? "" : " " + clientName.trim();
        var text = incomingContent == null ? "" : incomingContent.toLowerCase(Locale.ROOT);

        if (containsAny(text, "hola", "buenas", "buenos dias", "buenas tardes", "buenas noches")) {
            return "Hola%s, bienvenido a la tienda. ¿Qué te gustaría pedir hoy?".formatted(name);
        }
        if (containsAny(text, "precio", "cuanto cuesta", "cuánto cuesta", "costo", "vale")) {
            return "Con gusto te comparto los precios. ¿Qué producto te interesa?";
        }
        if (containsAny(text, "stock", "disponible", "tienen", "hay")) {
            return "Déjame revisar la disponibilidad. ¿De qué producto y qué cantidad necesitas?";
        }
        if (containsAny(text, "catalogo", "catálogo", "productos", "menu", "menú", "que venden", "qué venden")) {
            return "Te puedo mostrar el catálogo. Dime qué categoría buscas y te paso las opciones.";
        }
        if (containsAny(text, "pedido", "quiero", "comprar", "ordenar", "pedir")) {
            return "Perfecto, registro tu pedido. Indícame los productos, cantidades y tu dirección de entrega.";
        }
        if (containsAny(text, "pago", "yape", "plin", "transferencia", "comprobante", "pagar")) {
            return "Puedes enviarme el comprobante de pago por aquí y validamos tu pedido enseguida.";
        }
        if (containsAny(text, "gracias", "muchas gracias")) {
            return "¡A ti%s! Quedo atento para cualquier otro pedido.".formatted(name);
        }
        return "Gracias por tu mensaje%s. Un asesor revisará tu solicitud en breve. ¿Deseas hacer un pedido?"
                .formatted(name);
    }

    private static boolean containsAny(String text, String... keywords) {
        for (var keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
