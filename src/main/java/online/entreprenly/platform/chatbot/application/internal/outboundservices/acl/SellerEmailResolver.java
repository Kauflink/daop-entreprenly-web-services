package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import java.util.Optional;

/**
 * Outbound port (anti-corruption layer) that resolves a seller's account email from
 * their identifier, bridging the chatbot's {@code sellerId} with the email the Inventory
 * bounded context uses to own products.
 */
public interface SellerEmailResolver {

    /**
     * Resolves the account email of a seller.
     *
     * @param sellerId the seller identifier
     * @return the seller's email, if found
     */
    Optional<String> resolveEmail(Long sellerId);

    /**
     * Resolves the seller identifier that owns an account email. The reverse of
     * {@link #resolveEmail(Long)}, used to stamp WhatsApp sessions with the real
     * seller id when the bridge only knows the owner's email.
     *
     * @param email the seller's account email
     * @return the seller's identifier, if a matching account exists
     */
    Optional<Long> resolveSellerId(String email);
}
