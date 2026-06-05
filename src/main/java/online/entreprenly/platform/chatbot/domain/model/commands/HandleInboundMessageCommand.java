package online.entreprenly.platform.chatbot.domain.model.commands;

/**
 * Command representing an inbound WhatsApp message received from a client.
 *
 * @param fromPhone  the client's WhatsApp phone number
 * @param clientName the client's display name (nullable; derived from the phone when absent)
 * @param content    the message text
 */
public record HandleInboundMessageCommand(String fromPhone, String clientName, String content) {
}
