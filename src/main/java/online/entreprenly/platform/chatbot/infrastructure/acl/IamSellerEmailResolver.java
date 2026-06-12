package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SellerEmailResolver;
import online.entreprenly.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Anti-corruption layer adapter that resolves a seller's account email and identifier from
 * the IAM bounded context through its ACL facade, so the chatbot can use them to look up the
 * seller's catalog.
 */
@Service
public class IamSellerEmailResolver implements SellerEmailResolver {

    private final IamContextFacade iamContextFacade;

    public IamSellerEmailResolver(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    @Override
    public Optional<String> resolveEmail(Long sellerId) {
        if (sellerId == null) {
            return Optional.empty();
        }
        var email = iamContextFacade.fetchEmailByUserId(sellerId);
        return email.isBlank() ? Optional.empty() : Optional.of(email);
    }

    @Override
    public Optional<Long> resolveSellerId(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        var sellerId = iamContextFacade.fetchUserIdByEmail(email);
        return sellerId == 0L ? Optional.empty() : Optional.of(sellerId);
    }
}
