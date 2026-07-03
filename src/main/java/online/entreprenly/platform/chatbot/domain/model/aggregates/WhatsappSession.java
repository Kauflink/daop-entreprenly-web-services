package online.entreprenly.platform.chatbot.domain.model.aggregates;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.SessionStatus;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
public class WhatsappSession extends AbstractDomainAggregateRoot<WhatsappSession> {

    @Setter
    private Long id;
    private Long sellerId;
    private String phone;
    private String businessName;
    private SessionStatus status;
    private Instant connectedAt;
    private String qrCode;

    public WhatsappSession() {
    }

    public WhatsappSession(Long sellerId, String phone, String businessName, String qrCode) {
        this.sellerId = sellerId;
        this.phone = phone;
        this.businessName = businessName;
        this.status = SessionStatus.DISCONNECTED;
        this.qrCode = qrCode;
        this.connectedAt = null;
    }

    
    public void connect() {
        this.status = SessionStatus.CONNECTED;
        this.connectedAt = Instant.now();
    }

    
    public void disconnect() {
        this.status = SessionStatus.DISCONNECTED;
        this.connectedAt = null;
    }

    
    public void changeStatus(SessionStatus status) {
        if (status == null) return;
        switch (status) {
            case CONNECTED -> connect();
            case DISCONNECTED, EXPIRED -> {
                this.status = status;
                this.connectedAt = null;
            }
        }
    }

    
    public void restoreState(Long id, Long sellerId, String phone, String businessName,
                             SessionStatus status, Instant connectedAt, String qrCode) {
        this.id = id;
        this.sellerId = sellerId;
        this.phone = phone;
        this.businessName = businessName;
        this.status = status;
        this.connectedAt = connectedAt;
        this.qrCode = qrCode;
    }
}
