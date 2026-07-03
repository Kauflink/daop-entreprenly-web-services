package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;


public interface SubscriptionAccessChecker {

    String CHATBOT_FEATURE = "chatbot";

    
    boolean canUseChatbot(String ownerEmail);
}