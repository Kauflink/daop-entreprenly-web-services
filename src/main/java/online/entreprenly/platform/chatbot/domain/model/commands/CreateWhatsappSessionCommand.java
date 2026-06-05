package online.entreprenly.platform.chatbot.domain.model.commands;

/**
 * Command to register a new WhatsApp session for a seller.
 *
 * @param sellerId     the seller that owns the session
 * @param phone        the WhatsApp phone number
 * @param businessName the business display name shown to clients
 * @param qrCode       the pairing QR payload (nullable)
 */
public record CreateWhatsappSessionCommand(Long sellerId, String phone, String businessName, String qrCode) {
}
