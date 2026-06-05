package online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp;

import org.springframework.stereotype.Component;

/**
 * Holds the latest pairing QR and link state reported by the WhatsApp bridge,
 * so the frontend can render the real QR and know when the channel is connected.
 *
 * <p>Kept in memory on purpose: the QR is ephemeral (it rotates every few seconds)
 * and is only meaningful while the bridge is actively pairing.</p>
 */
@Component
public class WhatsAppBridgeState {

    private volatile String qr;
    private volatile boolean connected;

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
}
