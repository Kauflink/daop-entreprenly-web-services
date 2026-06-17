package online.entreprenly.platform.chatbot.domain.model.commands;


public record ReportBridgeConnectionCommand(boolean connected, String phone,
                                            String businessName, Long sellerId) {
}
