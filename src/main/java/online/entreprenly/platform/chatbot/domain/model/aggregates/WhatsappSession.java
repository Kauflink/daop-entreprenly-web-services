package online.entreprenly.platform.chatbot.domain.model.aggregates;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.SessionStatus;
import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * WhatsappSession aggregate root.
 *
 * <p>Represents the link between a seller and their WhatsApp number that the
 * chatbot uses to receive and send messages. The session owns its connection
 * lifecycle ({@link SessionStatus}) and exposes intention-revealing operations
 * to {@code connect} and {@code disconnect} rather than mutating status directly.</p>
 */
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

    /**
     * Marks the session as connected and stamps the connection instant.
     */
    public void connect() {
        this.status = SessionStatus.CONNECTED;
        this.connectedAt = Instant.now();
    }

    /**
     * Marks the session as disconnected and clears the connection instant.
     */
    public void disconnect() {
        this.status = SessionStatus.DISCONNECTED;
        this.connectedAt = null;
    }

    /**
     * Applies a desired status transition coming from an update command,
     * keeping the {@code connectedAt} stamp consistent with the new status.
     *
     * @param status the desired status (defaults to current when null)
     */
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

    /**
     * Restores an aggregate from persistence.
     */
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
