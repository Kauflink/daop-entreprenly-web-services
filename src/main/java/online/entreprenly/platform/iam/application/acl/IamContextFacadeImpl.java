package online.entreprenly.platform.iam.application.acl;

import online.entreprenly.platform.iam.application.commandservices.UserCommandService;
import online.entreprenly.platform.iam.application.queryservices.UserQueryService;
import online.entreprenly.platform.iam.domain.model.commands.SignUpCommand;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByEmailQuery;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByIdQuery;
import online.entreprenly.platform.iam.interfaces.acl.IamContextFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

/**
 * Application-layer implementation of the IAM ACL facade.
 *
 * <p>Provides a simplified integration surface for other bounded contexts that need identity
 * operations without coupling to IAM internal models.</p>
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacadeImpl(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Creates a new user through the IAM application command service.
     *
     * @param email    email to register
     * @param password raw password
     * @return created user identifier, or {@code 0L} when creation fails
     */
    public Long createUser(String email, String password) {
        var signUpCommand = new SignUpCommand(email, password, null, null, null, null);
        var result = userCommandService.handle(signUpCommand);
        return result.toOptional()
                .map(user -> user.getId())
                .orElse(0L);
    }

    /**
     * Fetches a user identifier by email through the IAM query service.
     *
     * @param email email to search
     * @return user identifier, or {@code 0L} when no user is found
     */
    public Long fetchUserIdByEmail(String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var user = userQueryService.handle(getUserByEmailQuery);
        return user.isEmpty() ? Long.valueOf(0L) : user.get().getId();
    }

    /**
     * Fetches a user email by identifier through the IAM query service.
     *
     * @param userId user identifier
     * @return email, or an empty string when no user is found
     */
    public String fetchEmailByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        return user.isEmpty() ? Strings.EMPTY : user.get().getEmail();
    }
}
