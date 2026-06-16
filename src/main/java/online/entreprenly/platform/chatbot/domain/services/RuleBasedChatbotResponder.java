package online.entreprenly.platform.chatbot.domain.services;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Rule-based implementation of {@link ChatbotResponder}.
 *
 * <p>Acts as the generic conversational fallback used when the catalog-aware
 * {@link ProductReplyComposer} and the order flow have nothing to say. It matches the
 * incoming text against a set of Spanish intents (greeting, payment methods, delivery,
 * hours, location, order status, help, complaints, farewell, thanks…) and returns a
 * helpful reply, keeping the conversation flowing without any paid NLP service.</p>
 *
 * <p>Matching is accent-insensitive and, for short ambiguous words ("si", "no", "ok"),
 * whole-word so they don't trigger inside longer words (e.g. "si" inside "necesito").</p>
 */
@Service
public class RuleBasedChatbotResponder implements ChatbotResponder {

    @Override
    public String reply(String incomingContent, String clientName) {
        var name = (clientName == null || clientName.isBlank()) ? "" : " " + clientName.trim();
        var text = normalize(incomingContent);
        var words = wordsOf(text);

        // Payment methods the client can use ("¿cómo pago?", "¿aceptan yape?").
        if (phrase(text, "como pago", "como puedo pagar", "formas de pago", "metodos de pago",
                "medios de pago", "forma de pago", "metodo de pago", "aceptan yape", "aceptan plin",
                "aceptan tarjeta", "aceptan transferencia", "puedo pagar con")) {
            return "Aceptamos Yape, Plin, transferencia bancaria y efectivo contra entrega. "
                    + "Cuando tengas tu pedido listo, envíame la captura del pago por aquí y lo validamos.";
        }

        // Already paid / sending a receipt.
        if (phrase(text, "comprobante", "ya pague", "ya pagué", "envie el pago", "envio el pago",
                "mi pago", "hice el pago", "hice la transferencia", "adjunto")) {
            return "Perfecto, envíame la captura o foto de tu comprobante por aquí y validamos tu pedido enseguida.";
        }

        // Delivery / shipping questions.
        if (phrase(text, "delivery", "hacen envios", "hacen envio", "envian", "reparto",
                "a domicilio", "cuanto demora", "cuanto tarda", "tiempo de entrega", "cuando llega",
                "en cuanto llega", "costo de envio", "cobran envio", "tienen delivery")) {
            return "Sí, hacemos delivery. El tiempo y costo dependen de tu zona. "
                    + "Indícame tu dirección y con gusto te confirmo el envío.";
        }

        // Opening hours.
        if (phrase(text, "horario", "que hora atienden", "a que hora abren", "a que hora cierran",
                "hasta que hora", "estan abiertos", "estan atendiendo", "atienden hoy", "abren hoy",
                "dias atienden", "que dias")) {
            return "Atendemos todos los días de 9:00 a 21:00. ¿En qué te puedo ayudar?";
        }

        // Store location.
        if (phrase(text, "donde estan", "donde quedan", "ubicacion", "su direccion", "tienen local",
                "tienda fisica", "como llego", "donde los encuentro", "donde se ubican")) {
            return "Trabajamos con entrega a domicilio. Pásame tu dirección y coordinamos la entrega de tu pedido.";
        }

        // Status of an existing order.
        if (phrase(text, "mi pedido", "mi orden", "estado de mi", "donde esta mi pedido",
                "ya salio mi", "cuando llega mi", "seguimiento", "rastrear")) {
            return "Déjame revisar el estado de tu pedido. ¿Me confirmas el número de pedido o el nombre con el que lo hiciste?";
        }

        // Talk to a human / help.
        if (phrase(text, "asesor", "hablar con alguien", "hablar con una persona", "una persona",
                "un humano", "atencion al cliente", "ayuda", "ayudame", "necesito ayuda", "soporte")) {
            return "Claro, con gusto te ayudo%s. Cuéntame qué necesitas y, si lo prefieres, derivo tu caso a un asesor.".formatted(name);
        }

        // Complaints / problems.
        if (phrase(text, "reclamo", "queja", "no llego", "no llegó", "esta mal", "llego mal",
                "llego incompleto", "problema con", "no funciona", "quiero devolver", "devolucion",
                "me cobraron", "esta dañado", "esta vencido")) {
            return "Lamento el inconveniente%s. Cuéntame qué ocurrió con tu pedido y lo resolvemos lo antes posible.".formatted(name);
        }

        // Is this a bot?
        if (phrase(text, "eres un bot", "eres bot", "eres real", "eres una persona", "eres humano",
                "robot", "con quien hablo", "eres una maquina")) {
            return "Soy el asistente virtual de la tienda%s. Puedo ayudarte con el catálogo, precios y pedidos. ¿Qué necesitas?".formatted(name);
        }

        // Pricing.
        if (phrase(text, "precio", "precios", "cuanto cuesta", "cuanto sale", "cuanto vale",
                "costo", "que precio")) {
            return "Con gusto te comparto los precios. ¿Qué producto te interesa?";
        }

        // Availability / stock.
        if (phrase(text, "stock", "disponible", "disponibilidad", "tienen", "tienes", "hay",
                "queda", "quedan", "consigo")) {
            return "Déjame revisar la disponibilidad. ¿De qué producto y qué cantidad necesitas?";
        }

        // Catalogue.
        if (phrase(text, "catalogo", "productos", "que venden", "que vendes", "menu", "lista de precios",
                "que ofrecen", "que tienen para", "muestrame")) {
            return "Te puedo mostrar el catálogo. Dime qué categoría buscas y te paso las opciones.";
        }

        // Ordering intent.
        if (phrase(text, "pedido", "quiero", "quisiera", "comprar", "ordenar", "pedir", "necesito",
                "llevar", "me gustaria", "adquirir")) {
            return "Perfecto, registro tu pedido. Indícame los productos, cantidades y tu dirección de entrega.";
        }

        // Thanks.
        if (phrase(text, "gracias", "muchas gracias", "mil gracias", "te agradezco")) {
            return "¡A ti%s! Quedo atento para cualquier otro pedido.".formatted(name);
        }

        // Farewell.
        if (phrase(text, "adios", "hasta luego", "nos vemos", "chau", "hasta pronto", "buen dia",
                "bye")) {
            return "¡Gracias por escribirnos%s! Que tengas un excelente día. Aquí estaré para tu próximo pedido.".formatted(name);
        }

        // Greeting (after substantive intents so "hola, ¿cuánto cuesta?" answers the question).
        if (phrase(text, "hola", "buenas", "buenos dias", "buenas tardes", "buenas noches",
                "hey", "saludos", "que tal", "alo")) {
            return "Hola%s, bienvenido a la tienda. ¿Qué te gustaría pedir hoy?".formatted(name);
        }

        // Short affirmations / negations.
        if (hasWord(words, "si", "claro", "dale", "ok", "okay", "listo", "perfecto", "correcto", "afirmativo")) {
            return "¡Genial! Cuéntame qué producto y cantidad deseas y armamos tu pedido.";
        }
        if (hasWord(words, "no", "nada", "ninguno", "ninguna")) {
            return "Entendido%s. Si necesitas algo más, aquí estoy para ayudarte con tu pedido.".formatted(name);
        }

        // Generic fallback.
        return "Gracias por tu mensaje%s. ¿Deseas hacer un pedido o conocer nuestros productos? "
                .formatted(name) + "Dime en qué te puedo ayudar.";
    }

    /** True when the (accent-normalised) text contains any of the given substrings/phrases. */
    private static boolean phrase(String text, String... keywords) {
        for (var keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /** True when the message has any of the given words as a whole token (for short ambiguous words). */
    private static boolean hasWord(Set<String> words, String... candidates) {
        for (var candidate : candidates) {
            if (words.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    private static Set<String> wordsOf(String text) {
        return new HashSet<>(Arrays.asList(text.split("[^a-z0-9]+")));
    }

    /** Lowercases and strips accents so matching is tolerant to diacritics. */
    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        var lower = value.toLowerCase(Locale.ROOT);
        var decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
