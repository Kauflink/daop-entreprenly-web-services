package online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ConnectedSellerProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multi-tenant in-memory store for the WhatsApp pairing QR and connection state
 * reported by the bridge, keyed by seller e-mail address.
 *
 * <p>Kept in memory on purpose: the QR is ephemeral and the bridge re-reports its
 * state (including the owner email) every time it (re)connects.</p>
 *
 * <p>Also implements {@link ConnectedSellerProvider} so the chatbot conversation
 * service can resolve the current owner email when no explicit email is carried
 * in the inbound message.</p>
 */
@Component
public class WhatsAppBridgeState implements ConnectedSellerProvider {

    /** Per-seller QR string (null once connected). */
    private final Map<String, String> qrByEmail       = new ConcurrentHashMap<>();
    /** Per-seller connection flag. */
    private final Map<String, Boolean> connectedByEmail = new ConcurrentHashMap<>();
    /** Last e-mail that reported a successful connection — used as fallback owner. */
    private volatile String lastConnectedEmail;

    // ── Multi-tenant write ────────────────────────────────────────────────────

    public void setQr(String email, String qr) {
        if (email == null || email.isBlank()) return;
        qrByEmail.put(email, qr);
        connectedByEmail.put(email, false);
    }

    public void setConnected(String email, boolean connected) {
        if (email == null || email.isBlank()) return;
        connectedByEmail.put(email, connected);
        if (connected) {
            qrByEmail.remove(email);   // QR is no longer useful once connected
            lastConnectedEmail = email;
        } else if (email.equals(lastConnectedEmail)) {
            lastConnectedEmail = null;
        }
    }

    // ── Multi-tenant read ─────────────────────────────────────────────────────

    public String getQr(String email) {
        return email == null ? null : qrByEmail.get(email);
    }

    public boolean isConnected(String email) {
        if (email == null) return false;
        return Boolean.TRUE.equals(connectedByEmail.get(email));
    }

    // ── ConnectedSellerProvider (single-email fallback for catalog resolution) ─

    @Override
    public Optional<String> currentOwnerEmail() {
        return Optional.ofNullable(lastConnectedEmail);
    }
}
