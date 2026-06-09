package online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ConnectedSellerProvider;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Holds the latest pairing QR, link state and connected seller email reported by the
 * WhatsApp bridge, so the frontend can render the real QR and the chatbot can resolve
 * the catalog owner from the connected channel.
 *
 * <p>Kept in memory on purpose: the QR is ephemeral and the bridge re-reports its
 * state (including the owner email) every time it connects.</p>
 */
@Component
public class WhatsAppBridgeState implements ConnectedSellerProvider {

    private volatile String qr;
    private volatile boolean connected;
    private volatile String ownerEmail;

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        if (connected) {
            // Once connected the QR is no longer useful.
            this.qr = null;
        }
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = (ownerEmail == null || ownerEmail.isBlank()) ? null : ownerEmail;
    }

    @Override
    public Optional<String> currentOwnerEmail() {
        return Optional.ofNullable(ownerEmail);
    }
}
