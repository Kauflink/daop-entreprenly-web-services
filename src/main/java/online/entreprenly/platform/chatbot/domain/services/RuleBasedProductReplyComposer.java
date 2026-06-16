package online.entreprenly.platform.chatbot.domain.services;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Rule-based {@link ProductReplyComposer}.
 *
 * <p>Matches the client message against the seller's catalog (by product name) and
 * answers with real price, stock and order totals. Recognises three intents: listing the
 * catalogue, asking about a product's price/availability, and ordering a quantity. When
 * nothing matches it returns empty so the generic responder takes over.</p>
 */
@Service
public class RuleBasedProductReplyComposer implements ProductReplyComposer {

    private static final Pattern NUMBER = Pattern.compile("(?<![\\w])(\\d+(?:[.,]\\d+)?)(?![\\w])");

    /** Spanish number words the client may use instead of digits ("quisiera tres"). */
    private static final Map<String, Double> NUMBER_WORDS = Map.ofEntries(
            Map.entry("un", 1.0), Map.entry("una", 1.0), Map.entry("uno", 1.0),
            Map.entry("dos", 2.0), Map.entry("tres", 3.0), Map.entry("cuatro", 4.0),
            Map.entry("cinco", 5.0), Map.entry("seis", 6.0), Map.entry("siete", 7.0),
            Map.entry("ocho", 8.0), Map.entry("nueve", 9.0), Map.entry("diez", 10.0),
            Map.entry("once", 11.0), Map.entry("doce", 12.0), Map.entry("media", 0.5),
            Map.entry("medio", 0.5));

    /** Verbs/expressions that signal the client wants to order a quantity. */
    private static final String[] ORDER_INTENT = {
            "quiero", "quisiera", "deseo", "dame", "necesito", "comprar", "pedir", "pedido",
            "llevar", "llevo", "ponme", "mandame", "enviame", "envieme", "vender", "me das",
            "kilos", "kilo", "kg", "unidad", "unidades"};

    @Override
    public Optional<String> compose(String incomingContent, List<CatalogProduct> catalog) {
        if (incomingContent == null || incomingContent.isBlank()) {
            return Optional.empty();
        }
        var text = normalize(incomingContent);
        var safeCatalog = catalog == null ? List.<CatalogProduct>of() : catalog;

        var match = bestMatch(text, safeCatalog);
        if (match != null) {
            return Optional.of(replyForProduct(text, match));
        }
        if (mentionsAny(text, "catalogo", "productos", "que venden", "que vende", "que vendes",
                "que tienes", "que hay", "menu", "lista", "ofrecen")) {
            return Optional.of(replyWithCatalogue(safeCatalog));
        }
        // The client asked about a product/availability, but nothing specific matched.
        if (isProductIntent(text)) {
            return Optional.of(replyWhenProductNotFound(safeCatalog));
        }
        return Optional.empty();
    }

    @Override
    public Optional<OrderItem> detectOrder(String incomingContent, List<CatalogProduct> catalog) {
        return detectOrder(incomingContent, catalog, null);
    }

    @Override
    public Optional<OrderItem> detectOrder(String incomingContent, List<CatalogProduct> catalog,
                                           CatalogProduct contextProduct) {
        if (incomingContent == null || incomingContent.isBlank() || catalog == null || catalog.isEmpty()) {
            return Optional.empty();
        }
        var text = normalize(incomingContent);

        // Prefer a product named in this very message; otherwise fall back to the product
        // the bot was last talking about (e.g. it just asked "¿cuántos deseas?").
        var product = bestMatch(text, catalog);
        boolean usingContext = false;
        if (product == null) {
            product = contextProduct;
            usingContext = true;
        }
        if (product == null) {
            return Optional.empty();
        }

        // With context we already know the product, so a bare quantity reply ("tres", "3")
        // is enough; without context we still require an explicit ordering intent.
        var quantity = usingContext ? anyQuantity(text, product) : orderQuantity(text, product);
        if (quantity.isEmpty()) {
            return Optional.empty();
        }
        double qty = quantity.get();
        if (qty <= 0 || !product.isInStock() || qty > product.availableStock()) {
            return Optional.empty();
        }
        return Optional.of(new OrderItem(product.name(), (int) Math.round(qty), product.price()));
    }

    @Override
    public Optional<CatalogProduct> matchProduct(String incomingContent, List<CatalogProduct> catalog) {
        if (incomingContent == null || incomingContent.isBlank() || catalog == null || catalog.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(bestMatch(normalize(incomingContent), catalog));
    }

    /** True when the message looks like a product or availability question. */
    private boolean isProductIntent(String text) {
        return mentionsAny(text, "tienen", "tiene", "tienes", "precio", "cuanto cuesta", "cuesta",
                "vale", "comprar", "vendes", "venden", "producto", "stock", "disponible",
                "disponibilidad", "mercaderia", "consigo", "venta");
    }

    /** Reply when no product matched: suggest the available catalogue, or say there is none. */
    private String replyWhenProductNotFound(List<CatalogProduct> catalog) {
        var inStock = catalog.stream().filter(CatalogProduct::isInStock).toList();
        if (inStock.isEmpty()) {
            return "Por ahora no contamos con productos disponibles. ¡Pronto tendremos novedades!";
        }
        var items = inStock.stream()
                .map(p -> "%s (%s %s)".formatted(p.name(), price(p.price()), p.soldByWeight() ? "por kg" : "c/u"))
                .collect(Collectors.joining(", "));
        return "No tenemos ese producto por ahora, pero sí contamos con: %s. ¿Te interesa alguno?".formatted(items);
    }

    private String replyForProduct(String text, CatalogProduct product) {
        var priceLabel = product.soldByWeight() ? "por kg" : "c/u";
        var unitLabel = product.soldByWeight() ? "kg" : "unidades";

        var quantity = orderQuantity(text, product);
        if (quantity.isPresent()) {
            double qty = quantity.get();
            if (!product.isInStock() || qty > product.availableStock()) {
                return "Por ahora solo tenemos %s de %s. ¿Deseas ajustar la cantidad?"
                        .formatted(stock(product), product.name());
            }
            double total = Math.round(qty * product.price() * 100.0) / 100.0;
            return "Perfecto. %s de %s = %s. ¿Confirmas el pedido? ¿A qué dirección lo enviamos?"
                    .formatted(quantityLabel(qty, unitLabel), product.name(), price(total));
        }

        if (!product.isInStock()) {
            return "%s cuesta %s %s, pero por ahora no tenemos stock disponible."
                    .formatted(product.name(), price(product.price()), priceLabel);
        }
        return "Sí, tenemos %s a %s %s. Quedan %s disponibles. ¿Cuántos deseas?"
                .formatted(product.name(), price(product.price()), priceLabel, stock(product));
    }

    private String replyWithCatalogue(List<CatalogProduct> catalog) {
        var items = catalog.stream()
                .filter(CatalogProduct::isInStock)
                .map(p -> "%s (%s %s)".formatted(p.name(), price(p.price()), p.soldByWeight() ? "por kg" : "c/u"))
                .collect(Collectors.joining(", "));
        if (items.isBlank()) {
            return "Por ahora no tenemos productos con stock disponible.";
        }
        return "Tenemos disponible: %s. ¿Qué te gustaría pedir?".formatted(items);
    }

    /** Returns the catalog product whose name best matches the message, or null. */
    private CatalogProduct bestMatch(String text, List<CatalogProduct> catalog) {
        CatalogProduct best = null;
        int bestScore = 0;
        for (var product : catalog) {
            int score = 0;
            for (var token : normalize(product.name()).split("\\s+")) {
                if (token.length() >= 3 && text.contains(token)) {
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                best = product;
            }
        }
        return best;
    }

    /** Extracts an ordered quantity when the message expresses an ordering intent. */
    private Optional<Double> orderQuantity(String text, CatalogProduct product) {
        if (!mentionsAny(text, ORDER_INTENT)) {
            return Optional.empty();
        }
        return anyQuantity(text, product);
    }

    /**
     * Extracts a quantity from the message regardless of intent keywords, accepting both
     * digits ("3", "2.5") and Spanish number words ("tres"). Used when the product is
     * already known from the conversation context, so a bare number is a valid reply.
     */
    private Optional<Double> anyQuantity(String text, CatalogProduct product) {
        // Remove the product name tokens so we don't read a number that is part of the name.
        var cleaned = text;
        for (var token : normalize(product.name()).split("\\s+")) {
            if (token.length() >= 3) {
                cleaned = cleaned.replace(token, " ");
            }
        }
        Matcher matcher = NUMBER.matcher(cleaned);
        if (matcher.find()) {
            return Optional.of(Double.parseDouble(matcher.group(1).replace(',', '.')));
        }
        // No digits: try a Spanish number word ("quisiera tres").
        for (var word : cleaned.split("[^a-zñ]+")) {
            var value = NUMBER_WORDS.get(word);
            if (value != null) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    private String stock(CatalogProduct product) {
        return product.soldByWeight()
                ? String.format(Locale.US, "%.1f kg", product.availableStock())
                : String.format(Locale.US, "%d unidades", (long) product.availableStock());
    }

    private String quantityLabel(double quantity, String unitLabel) {
        return unitLabel.equals("kg")
                ? String.format(Locale.US, "%.1f kg", quantity)
                : String.format(Locale.US, "%d unidades", (long) quantity);
    }

    // Locale.US so the decimal separator is always a dot, regardless of the host locale.
    private static String price(double value) {
        return String.format(Locale.US, "S/%.2f", value);
    }

    private static boolean mentionsAny(String text, String... keywords) {
        for (var keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /** Lowercases and strips accents so matching is tolerant to diacritics. */
    private static String normalize(String value) {
        var lower = value.toLowerCase(Locale.ROOT);
        var decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
