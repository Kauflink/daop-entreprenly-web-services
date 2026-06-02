package online.entreprenly.platform.iam.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.iam.domain.model.aggregates.User;
import online.entreprenly.platform.iam.domain.repositories.UserRepository;
import online.entreprenly.platform.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;
import online.entreprenly.platform.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the IAM user domain repository port with Spring Data JPA.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserPersistenceRepository userPersistenceRepository;

    public UserRepositoryImpl(UserPersistenceRepository userPersistenceRepository) {
        this.userPersistenceRepository = userPersistenceRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userPersistenceRepository.findById(id).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userPersistenceRepository.findByEmail(email).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<User> findAll() {
        return userPersistenceRepository.findAll().stream().map(UserPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public User save(User user) {
        var saved = userPersistenceRepository.save(UserPersistenceAssembler.toPersistenceFromDomain(user));
        return UserPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userPersistenceRepository.existsByEmail(email);
    }
}

