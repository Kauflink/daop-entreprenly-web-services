package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import java.util.Optional;

/**
 * Outbound port exposing the account email of the seller currently connected through
 * the WhatsApp bridge. Lets the chatbot resolve the catalog owner directly from the
 * connected channel, instead of guessing from a numeric seller id.
 */
public interface ConnectedSellerProvider {

    /**
     * @return the connected seller's account email, if the bridge reported one
     */
    Optional<String> currentOwnerEmail();
}
