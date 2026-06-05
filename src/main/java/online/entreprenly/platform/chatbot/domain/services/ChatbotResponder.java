package online.entreprenly.platform.chatbot.domain.services;

/**
 * Domain service that produces the chatbot's automatic reply to a client message.
 *
 * <p>Kept as an abstraction so the reply strategy (currently rule-based) can evolve
 * without affecting the conversation orchestration.</p>
 */
public interface ChatbotResponder {

    /**
     * Produces an automatic reply for an inbound client message.
     *
     * @param incomingContent the client's message text
     * @param clientName      the client's display name (nullable)
     * @return the bot reply text
     */
    String reply(String incomingContent, String clientName);
}
