package online.entreprenly.platform.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables Spring Data JPA auditing so that {@code @CreatedDate} and
 * {@code @LastModifiedDate} fields (see
 * {@link online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity})
 * are populated automatically on persist and update.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
