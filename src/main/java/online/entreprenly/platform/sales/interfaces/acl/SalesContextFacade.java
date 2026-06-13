package online.entreprenly.platform.sales.interfaces.acl;

import java.util.List;

/**
 * ACL facade that exposes Sales bounded context capabilities to other contexts.
 *
 * <p>Provides a simplified integration surface for registering a sale and updating the daily
 * cash register, without leaking the Sales internal model.</p>
 */
public interface SalesContextFacade {
    /**
     * Registers a completed sale for the given seller and updates the seller's daily cash
     * register with the sale total.
     *
     * @param ownerEmail the seller's account email that owns the sale
     * @param sellerId   the seller identifier
     * @param lines      the sale lines
     * @param total      the sale total
     * @return {@code true} when the sale was registered, {@code false} otherwise
     */
    boolean registerChatSale(String ownerEmail, Long sellerId, List<ChatSaleLine> lines, double total);
}
