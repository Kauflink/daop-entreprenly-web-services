package online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ConnectedSellerProvider;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds the latest pairing QR and link state reported by the WhatsApp bridge,
 * keyed by seller e-mail so multiple sellers can pair simultaneously.
 *
 * <p>Implements {@link ConnectedSellerProvider} as a no-op fallback: the multi-tenant
 * bridge always sends {@code ownerEmail} in every message body, so the catalog resolver
 * uses that directly. {@code currentOwnerEmail()} is kept for backward-compat only.</p>
 *
 * <p>Kept in memory: the QR is ephemeral and only meaningful while pairing.</p>
 */
@Component
public class WhatsAppBridgeState implements ConnectedSellerProvider {

    private record SellerState(String qr, boolean connected) {}

    private final ConcurrentHashMap<String, SellerState> states = new ConcurrentHashMap<>();

    /** Called by the bridge when a new QR is generated for a seller. */
    public void setQr(String ownerEmail, String qr) {
        states.put(ownerEmail, new SellerState(qr, false));
    }

    /** Called by the bridge when a seller's WhatsApp connects or disconnects. */
    public void setConnected(String ownerEmail, boolean connected) {
        SellerState current = states.getOrDefault(ownerEmail, new SellerState(null, false));
        // Clear QR once connected (no longer needed).
        states.put(ownerEmail, new SellerState(connected ? null : current.qr(), connected));
    }

    /** Returns the current QR for a seller (null if none or already connected). */
    public String getQr(String ownerEmail) {
        SellerState s = states.get(ownerEmail);
        return s != null ? s.qr() : null;
    }

    /** Returns whether a seller's WhatsApp is currently linked. */
    public boolean isConnected(String ownerEmail) {
        SellerState s = states.get(ownerEmail);
        return s != null && s.connected();
    }

    /**
     * No-op fallback: the multi-tenant bridge always sends ownerEmail per message,
     * so the catalog resolver never needs to fall back to this.
     */
    @Override
    public Optional<String> currentOwnerEmail() {
        return Optional.empty();
    }
}
