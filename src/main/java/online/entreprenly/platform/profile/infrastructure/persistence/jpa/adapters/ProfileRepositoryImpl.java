package online.entreprenly.platform.profile.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.profile.domain.model.aggregates.Profile;
import online.entreprenly.platform.profile.domain.repositories.ProfileRepository;
import online.entreprenly.platform.profile.infrastructure.persistence.jpa.assemblers.ProfilePersistenceAssembler;
import online.entreprenly.platform.profile.infrastructure.persistence.jpa.repositories.ProfilePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository adapter that bridges the profile domain repository port with Spring Data JPA.
 */
@Repository
public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfilePersistenceRepository profilePersistenceRepository;

    public ProfileRepositoryImpl(ProfilePersistenceRepository profilePersistenceRepository) {
        this.profilePersistenceRepository = profilePersistenceRepository;
    }

    @Override
    public Optional<Profile> findById(Long id) {
        return profilePersistenceRepository.findById(id).map(ProfilePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Profile> findByUserId(Long userId) {
        return profilePersistenceRepository.findByUserId(userId).map(ProfilePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return profilePersistenceRepository.existsByUserId(userId);
    }

    @Override
    public Profile save(Profile profile) {
        var saved = profilePersistenceRepository.save(ProfilePersistenceAssembler.toPersistenceFromDomain(profile));
        return ProfilePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
