package online.entreprenly.platform.chatbot.interfaces.rest;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.SubscriptionAccessChecker;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class ChatbotSubscriptionGuard {

    private final SubscriptionAccessChecker subscriptionAccessChecker;

    public ChatbotSubscriptionGuard(SubscriptionAccessChecker subscriptionAccessChecker) {
        this.subscriptionAccessChecker = subscriptionAccessChecker;
    }

    public boolean canAccess(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && subscriptionAccessChecker.canUseChatbot(authentication.getName());
    }

    public boolean canAccessOwner(String ownerEmail) {
        return subscriptionAccessChecker.canUseChatbot(ownerEmail);
    }
}