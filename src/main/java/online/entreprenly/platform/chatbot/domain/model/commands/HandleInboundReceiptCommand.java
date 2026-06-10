package online.entreprenly.platform.chatbot.domain.model.commands;

/**
 * Command representing an inbound WhatsApp payment receipt (image) sent by a client.
 *
 * @param fromPhone  the client's WhatsApp phone number
 * @param ownerEmail the seller's account email reported by the bridge (nullable)
 * @param image      the receipt image as a data URL
 */
public record HandleInboundReceiptCommand(String fromPhone, String ownerEmail, String image) {
}
