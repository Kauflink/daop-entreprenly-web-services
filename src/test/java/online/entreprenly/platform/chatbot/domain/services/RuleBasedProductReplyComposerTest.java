package online.entreprenly.platform.chatbot.domain.services;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RuleBasedProductReplyComposerTest {

    private final ProductReplyComposer composer = new RuleBasedProductReplyComposer();

    private static final List<CatalogProduct> CATALOG = List.of(
            new CatalogProduct("Manzana", 4.50, true, 20.0),
            new CatalogProduct("Pan", 0.50, false, 30.0),
            new CatalogProduct("Coca Cola 500ml", 2.50, false, 0.0));

    @Test
    @DisplayName("answers price and stock for a product question")
    void answersPriceAndStock() {
        var reply = composer.compose("¿Tienen manzana? ¿a cuánto?", CATALOG);
        assertThat(reply).isPresent();
        assertThat(reply.get()).contains("4.50").contains("20.0 kg");
    }

    @Test
    @DisplayName("computes the total for an order quantity")
    void computesOrderTotal() {
        var reply = composer.compose("Hola, quiero 5 kilos de manzana", CATALOG);
        assertThat(reply).isPresent();
        assertThat(reply.get()).contains("22.50");
    }

    @Test
    @DisplayName("warns when the requested quantity exceeds stock")
    void warnsWhenOverStock() {
        var reply = composer.compose("quiero 50 kilos de manzana", CATALOG);
        assertThat(reply).isPresent();
        assertThat(reply.get()).contains("20.0 kg").containsIgnoringCase("ajustar");
    }

    @Test
    @DisplayName("lists the available catalogue, hiding out-of-stock products")
    void listsCatalogue() {
        var reply = composer.compose("¿qué venden?", CATALOG);
        assertThat(reply).isPresent();
        assertThat(reply.get()).contains("Manzana").contains("Pan").doesNotContain("Coca Cola");
    }

    @Test
    @DisplayName("reports when a known product is out of stock")
    void reportsOutOfStock() {
        var reply = composer.compose("tienen coca cola?", CATALOG);
        assertThat(reply).isPresent();
        assertThat(reply.get()).containsIgnoringCase("no tenemos stock");
    }

    @Test
    @DisplayName("returns empty for non-product messages so the generic reply takes over")
    void fallsBackForNonProduct() {
        assertThat(composer.compose("buenas tardes", CATALOG)).isEmpty();
        assertThat(composer.compose("hola, gracias", List.of())).isEmpty();
    }

    @Test
    @DisplayName("suggests the available catalogue when the asked product is not found")
    void suggestsWhenProductNotFound() {
        var reply = composer.compose("tienen pepsi?", CATALOG);
        assertThat(reply).isPresent();
        assertThat(reply.get()).containsIgnoringCase("no tenemos ese producto").contains("Manzana");
    }

    @Test
    @DisplayName("informs when the seller has no products at all")
    void informsWhenNoProducts() {
        var reply = composer.compose("tienen pepsi?", List.of());
        assertThat(reply).isPresent();
        assertThat(reply.get()).containsIgnoringCase("no contamos con productos");
    }
}
