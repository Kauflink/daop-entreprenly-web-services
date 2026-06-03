package online.entreprenly.platform.profile.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.profile.infrastructure.persistence.jpa.embeddables.NotificationSettingsEmbeddable;
import online.entreprenly.platform.profile.infrastructure.persistence.jpa.embeddables.PreferencesEmbeddable;
import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for profiles.
 */
@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class ProfilePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;

    @Column(name = "role", length = 60)
    private String role;

    @Column(name = "plan", length = 60)
    private String plan;

    @Embedded
    private PreferencesEmbeddable preferences;

    @Embedded
    private NotificationSettingsEmbeddable notificationSettings;
}
