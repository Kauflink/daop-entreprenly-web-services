package online.entreprenly.platform.chatbot.application.internal.outboundservices.whatsapp;

/**
 * Outbound port for sending messages to the WhatsApp channel.
 *
 * <p>The application layer depends on this abstraction only; the concrete adapter
 * is selected by configuration. The default adapter is a no-op logging stub so the
 * chatbot works end-to-end locally without incurring any cost or requiring external
 * credentials. A real WhatsApp Cloud API adapter can be plugged in later behind the
 * same port.</p>
 */
public interface WhatsAppMessagingService {

    /**
     * Sends a text message to a client's WhatsApp number.
     *
     * @param toPhone the destination phone number in international format
     * @param content the message body
     * @return {@code true} when the message was accepted by the channel
     */
    boolean sendText(String toPhone, String content);
}
