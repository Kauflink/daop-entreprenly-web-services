package online.entreprenly.platform.chatbot.domain.services;


public interface ChatbotResponder {

    
    String reply(String incomingContent, String clientName);
}
