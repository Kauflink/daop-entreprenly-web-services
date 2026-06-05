package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;
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
 * JPA persistence entity for conversations.
 */
@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
public class ConversationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "client_phone", nullable = false, length = 30)
    private String clientPhone;

    @Column(name = "client_name", length = 120)
    private String clientName;

    @Column(name = "last_message", length = 500)
    private String lastMessage;

    @Column(name = "last_message_time", length = 20)
    private String lastMessageTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ConversationStatus status;

    @Column(name = "conversation_created_at")
    private Instant conversationCreatedAt;

    @Column(name = "closed_at")
    private Instant closedAt;
}
