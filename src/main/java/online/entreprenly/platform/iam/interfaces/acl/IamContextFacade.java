package online.entreprenly.platform.iam.interfaces.acl;

import online.entreprenly.platform.iam.application.commandservices.UserCommandService;
import online.entreprenly.platform.iam.application.queryservices.UserQueryService;
import online.entreprenly.platform.iam.domain.model.commands.SignUpCommand;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByEmailQuery;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByIdQuery;
import org.apache.logging.log4j.util.Strings;

/**
 * ACL facade that exposes IAM bounded context capabilities to other contexts.
 *
 * <p>Provides a simplified integration surface for creating users and querying identity data
 * without leaking IAM internal model details.</p>
 */
public class IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacade(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Creates a new user with the default role.
     *
     * @param email    email to register
     * @param password raw password
     * @return created user identifier, or {@code 0L} when creation fails
     */
    public Long createUser(String email, String password) {
        var signUpCommand = new SignUpCommand(email, password);
        var result = userCommandService.handle(signUpCommand);
        if (result instanceof online.entreprenly.platform.shared.application.result.Result.Success(var user)) {
            return user.getId();
        }
        return 0L;
    }

    /**
     * Fetches the identifier for an email.
     *
     * @param email email to search
     * @return user identifier, or {@code 0L} when user is not found
     */
    public Long fetchUserIdByEmail(String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var result = userQueryService.handle(getUserByEmailQuery);
        if (result.isEmpty()) return 0L;
        return result.get().getId();
    }

    /**
     * Fetches the email for a user identifier.
     *
     * @param userId user identifier
     * @return email, or an empty string when user is not found
     */
    public String fetchEmailByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        if (result.isEmpty()) return Strings.EMPTY;
        return result.get().getEmail();
    }

}
