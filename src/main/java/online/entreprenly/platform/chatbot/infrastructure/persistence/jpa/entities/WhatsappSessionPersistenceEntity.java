package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.SessionStatus;
import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA persistence entity for WhatsApp sessions.
 */
@Entity
@Table(name = "whatsapp_sessions")
@Getter
@Setter
@NoArgsConstructor
public class WhatsappSessionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Column(name = "business_name", length = 120)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SessionStatus status;

    @Column(name = "connected_at")
    private Instant connectedAt;

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;
}
