package online.entreprenly.platform.chatbot.domain.services;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;

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
}
