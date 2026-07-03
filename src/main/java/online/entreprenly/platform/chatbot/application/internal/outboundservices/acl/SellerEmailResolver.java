package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import java.util.Optional;


public interface SellerEmailResolver {

    
    Optional<String> resolveEmail(Long sellerId);

    
    Optional<Long> resolveSellerId(String email);
}
