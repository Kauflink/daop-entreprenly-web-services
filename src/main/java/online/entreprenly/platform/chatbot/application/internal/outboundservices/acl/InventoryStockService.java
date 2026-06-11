package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;

import java.util.List;

/**
 * Outbound port (anti-corruption layer) that lets the chatbot deduct sold quantities
 * from a seller's stock in the Inventory bounded context.
 *
 * <p>Invoked when a chat order is confirmed (payment approved), so the available stock
 * computed from inventory lots reflects what has actually been sold. The implementation
 * matches each {@link OrderItem} to the seller's product by name and reduces its lots,
 * keeping the chatbot decoupled from the inventory domain model.</p>
 */
public interface InventoryStockService {

    /**
     * Deducts the quantities of a confirmed order from the seller's stock.
     *
     * <p>Best-effort and idempotent at the call site: items whose product cannot be
     * found are skipped, and a lot is never driven below zero.</p>
     *
     * @param ownerEmail the seller's account email that owns the products
     * @param items      the confirmed order lines to deduct
     */
    void decrementForOrder(String ownerEmail, List<OrderItem> items);
}
