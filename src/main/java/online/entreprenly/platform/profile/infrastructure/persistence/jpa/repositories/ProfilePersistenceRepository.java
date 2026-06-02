package online.entreprenly.platform.profile.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.profile.infrastructure.persistence.jpa.entities.ProfilePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for profile persistence entities.
 */
@Repository
public interface ProfilePersistenceRepository extends JpaRepository<ProfilePersistenceEntity, Long> {

    /**
     * Finds a profile by the IAM user identifier it belongs to.
     *
     * @param userId the IAM user identifier
     * @return the profile, if present
     */
    Optional<ProfilePersistenceEntity> findByUserId(Long userId);

    /**
     * Checks whether a profile already exists for the given IAM user identifier.
     *
     * @param userId the IAM user identifier
     * @return {@code true} if a profile exists, otherwise {@code false}
     */
    boolean existsByUserId(Long userId);
}
