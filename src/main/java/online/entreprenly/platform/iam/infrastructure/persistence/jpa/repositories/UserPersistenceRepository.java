package online.entreprenly.platform.iam.infrastructure.persistence.jpa.repositories;

import online.entreprenly.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for IAM user persistence entities.
 */
@Repository
public interface UserPersistenceRepository extends JpaRepository<UserPersistenceEntity, Long>
{
    /**
     * This method is responsible for finding the user by email.
     * @param email The email.
     * @return The user object.
     */
    Optional<UserPersistenceEntity> findByEmail(String email);

    /**
     * This method is responsible for checking if the user exists by email.
     * @param email The email.
     * @return True if the user exists, false otherwise.
     */
    boolean existsByEmail(String email);

}

