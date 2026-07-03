package online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp;


public interface WhatsAppMessagingService {

    
    boolean sendText(String ownerEmail, String toPhone, String content);
}
