package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.iam.application.queryservices.UserQueryService;
import online.entreprenly.platform.iam.domain.model.aggregates.User;
import online.entreprenly.platform.iam.domain.model.queries.GetUserByIdQuery;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Anti-corruption layer adapter that resolves a seller's account email from the IAM
 * bounded context, so the chatbot can use it to look up the seller's catalog.
 */
@Service
public class IamSellerEmailResolver implements SellerEmailResolver {

    private final UserQueryService userQueryService;

    public IamSellerEmailResolver(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public Optional<String> resolveEmail(Long sellerId) {
        if (sellerId == null) {
            return Optional.empty();
        }
        return userQueryService.handle(new GetUserByIdQuery(sellerId)).map(User::getEmail);
    }
}
