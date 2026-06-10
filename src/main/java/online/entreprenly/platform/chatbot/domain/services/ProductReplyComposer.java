package online.entreprenly.platform.chatbot.domain.services;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;

import java.util.List;
import java.util.Optional;

/**
 * Domain service that tries to answer a client message using the seller's real catalog
 * (price, stock and totals). When the message is not about products it returns an empty
 * result so the caller can fall back to the generic conversational reply.
 */
public interface ProductReplyComposer {

    /**
     * Builds a product-aware reply for an inbound message, if applicable.
     *
     * @param incomingContent the client's message text
     * @param catalog         the seller's catalog snapshot
     * @return a reply grounded in real product data, or empty when not a product question
     */
    Optional<String> compose(String incomingContent, List<CatalogProduct> catalog);

    /**
     * Detects a concrete purchase intent (a known product with a quantity that is in stock).
     *
     * @param incomingContent the client's message text
     * @param catalog         the seller's catalog snapshot
     * @return the requested order line, or empty when the message is not a confirmable order
     */
    Optional<OrderItem> detectOrder(String incomingContent, List<CatalogProduct> catalog);

    /**
     * Detects a purchase intent that continues a previous turn, binding a bare quantity
     * (e.g. "quisiera tres" or just "3") to the product the bot was last talking about.
     *
     * <p>When the message already names a product it behaves like
     * {@link #detectOrder(String, List)}; otherwise the {@code contextProduct} is used.</p>
     *
     * @param incomingContent the client's message text
     * @param catalog         the seller's catalog snapshot
     * @param contextProduct  the product discussed in the previous turn (nullable)
     * @return the requested order line, or empty when no confirmable quantity is present
     */
    Optional<OrderItem> detectOrder(String incomingContent, List<CatalogProduct> catalog, CatalogProduct contextProduct);

    /**
     * Returns the catalog product the message is most likely about, so the caller can
     * remember it as conversation context for the next turn.
     *
     * @param incomingContent the client's message text
     * @param catalog         the seller's catalog snapshot
     * @return the best-matching product, or empty when none is mentioned
     */
    Optional<CatalogProduct> matchProduct(String incomingContent, List<CatalogProduct> catalog);
}
