package online.entreprenly.platform.chatbot.domain.model.commands;


public record CreateWhatsappSessionCommand(Long sellerId, String phone, String businessName, String qrCode) {
}
