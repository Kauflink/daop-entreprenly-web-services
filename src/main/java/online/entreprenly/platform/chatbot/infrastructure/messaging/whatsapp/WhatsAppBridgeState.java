package online.entreprenly.platform.chatbot.infrastructure.messaging.whatsapp;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ConnectedSellerProvider;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class WhatsAppBridgeState implements ConnectedSellerProvider {

    private record SellerState(String qr, boolean connected) {}

    private final ConcurrentHashMap<String, SellerState> states = new ConcurrentHashMap<>();

    
    public void setQr(String ownerEmail, String qr) {
        states.put(ownerEmail, new SellerState(qr, false));
    }

    
    public void setConnected(String ownerEmail, boolean connected) {
        SellerState current = states.getOrDefault(ownerEmail, new SellerState(null, false));
        
        states.put(ownerEmail, new SellerState(connected ? null : current.qr(), connected));
    }

    
    public String getQr(String ownerEmail) {
        SellerState s = states.get(ownerEmail);
        return s != null ? s.qr() : null;
    }

    
    public boolean isConnected(String ownerEmail) {
        SellerState s = states.get(ownerEmail);
        return s != null && s.connected();
    }

    
    public void clear(String ownerEmail) {
        states.remove(ownerEmail);
    }

    @Override
    public Optional<String> currentOwnerEmail() {
        return Optional.empty();
    }
}
