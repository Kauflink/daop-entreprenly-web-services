package online.entreprenly.platform.chatbot.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;
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


@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessagePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender", length = 20)
    private MessageSender sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private MessageType type;

    @Column(name = "sent_at")
    private Instant sentAt;
}
