package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

/**
 * Outbound port that checks whether a seller has access to chatbot capabilities.
 */
public interface SubscriptionAccessChecker {

    String CHATBOT_FEATURE = "chatbot";

    /**
     * Checks whether the account identified by email can use the chatbot feature.
     *
     * @param ownerEmail authenticated seller email
     * @return true when the seller has an active subscription with chatbot access
     */
    boolean canUseChatbot(String ownerEmail);
}