package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import java.util.Optional;


public interface ConnectedSellerProvider {

    
    Optional<String> currentOwnerEmail();
}
