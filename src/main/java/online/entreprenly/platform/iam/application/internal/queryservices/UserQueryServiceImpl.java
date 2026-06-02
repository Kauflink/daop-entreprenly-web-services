package online.entreprenly.platform.iam.application.internal.queryservices;

import online.entreprenly.platform.iam.application.queryservices.UserQueryService;
import online.entreprenly.platform.iam.domain.model.aggregates.User;
import online.entreprenly.platform.iam.domain.model.queries.GetAllUsersQuery;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByIdQuery;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByEmailQuery;
import online.entreprenly.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves IAM user read queries.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }
}
