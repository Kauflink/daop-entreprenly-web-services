package online.entreprenly.platform.chatbot.domain.model.commands;

/**
 * Command reported by the WhatsApp bridge when its link state changes.
 *
 * <p>Used to create (when first connected) or update the seller's
 * {@code WhatsappSession} so the dashboard reflects the real connection.</p>
 *
 * @param connected    whether the bridge is currently linked to WhatsApp
 * @param phone        the linked WhatsApp phone number
 * @param businessName the business display name
 * @param sellerId     the seller that owns the session
 */
public record ReportBridgeConnectionCommand(boolean connected, String phone,
                                            String businessName, Long sellerId) {
}
