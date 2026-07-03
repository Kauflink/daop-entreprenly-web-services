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


@Service
public class RuleBasedProductReplyComposer implements ProductReplyComposer {

    private static final Pattern NUMBER = Pattern.compile("(?<![\\w])(\\d+(?:[.,]\\d+)?)(?![\\w])");

    
    private static final Map<String, Double> NUMBER_WORDS = Map.ofEntries(
            Map.entry("un", 1.0), Map.entry("una", 1.0), Map.entry("uno", 1.0),
            Map.entry("dos", 2.0), Map.entry("tres", 3.0), Map.entry("cuatro", 4.0),
            Map.entry("cinco", 5.0), Map.entry("seis", 6.0), Map.entry("siete", 7.0),
            Map.entry("ocho", 8.0), Map.entry("nueve", 9.0), Map.entry("diez", 10.0),
            Map.entry("once", 11.0), Map.entry("doce", 12.0), Map.entry("media", 0.5),
            Map.entry("medio", 0.5));

    
    private static final String[] ORDER_INTENT = {
            "quiero", "quisiera", "deseo", "dame", "necesito", "comprar", "pedir", "pedido",
            "llevar", "llevo", "ponme", "mandame", "enviame", "envieme", "vender", "me das",
            "kilos", "kilo", "kg", "unidad", "unidades"};

    
    private static final List<String> EXTRACTION_STOP_WORDS = List.of(
            "quiero", "quisiera", "deseo", "dame", "necesito", "comprar", "pedir", "pedido",
            "llevar", "llevo", "ponme", "mandame", "enviame", "envieme", "vender", "das",
            "kilos", "kilo", "unidad", "unidades",
            "un", "una", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho",
            "nueve", "diez", "once", "doce", "media", "medio",
            "de", "del", "la", "el", "los", "las", "por", "favor", "porfavor", "me", "kg");

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
        
        if (mentionsAny(text, ORDER_INTENT)) {
            return Optional.of(replyWhenProductNotFound(safeCatalog, extractRequestedProduct(text)));
        }
        
        if (isProductIntent(text)) {
            return Optional.of(replyWhenProductNotFound(safeCatalog, null));
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

        
        
        var product = bestMatch(text, catalog);
        boolean usingContext = false;
        if (product == null) {
            product = contextProduct;
            usingContext = true;
        }
        if (product == null) {
            return Optional.empty();
        }

        
        
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

    
    private boolean isProductIntent(String text) {
        return mentionsAny(text, "tienen", "tiene", "tienes", "precio", "cuanto cuesta", "cuesta",
                "vale", "comprar", "vendes", "venden", "producto", "stock", "disponible",
                "disponibilidad", "mercaderia", "consigo", "venta");
    }

    
    private String replyWhenProductNotFound(List<CatalogProduct> catalog, String requestedProduct) {
        var inStock = catalog.stream().filter(CatalogProduct::isInStock).toList();
        if (inStock.isEmpty()) {
            if (requestedProduct != null && !requestedProduct.isBlank()) {
                return "Lo sentimos, no contamos con %s ni con otros productos disponibles en este momento. ¿Puedo ayudarte con algo más?"
                        .formatted(requestedProduct);
            }
            return "Por ahora no contamos con productos disponibles. ¡Pronto tendremos novedades!";
        }
        var items = inStock.stream()
                .map(p -> "%s (%s %s)".formatted(p.name(), price(p.price()), p.soldByWeight() ? "por kg" : "c/u"))
                .collect(Collectors.joining(", "));
        if (requestedProduct != null && !requestedProduct.isBlank()) {
            return "No contamos con %s, pero sí tenemos: %s. ¿Te interesa alguno?".formatted(requestedProduct, items);
        }
        return "No tenemos ese producto por ahora, pero sí contamos con: %s. ¿Te interesa alguno?".formatted(items);
    }

    
    private String extractRequestedProduct(String text) {
        var sb = new StringBuilder();
        for (var word : text.split("[^a-zñ]+")) {
            if (word.length() >= 3 && !EXTRACTION_STOP_WORDS.contains(word)) {
                if (!sb.isEmpty()) sb.append(" ");
                sb.append(word);
            }
        }
        return sb.isEmpty() ? null : sb.toString();
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

    
    private Optional<Double> orderQuantity(String text, CatalogProduct product) {
        if (!mentionsAny(text, ORDER_INTENT)) {
            return Optional.empty();
        }
        return anyQuantity(text, product);
    }

    
    private Optional<Double> anyQuantity(String text, CatalogProduct product) {
        
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

    
    private static String normalize(String value) {
        var lower = value.toLowerCase(Locale.ROOT);
        var decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
