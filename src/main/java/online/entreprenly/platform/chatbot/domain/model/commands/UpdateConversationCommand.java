package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;


public record UpdateConversationCommand(Long conversationId, ConversationStatus status,
                                        String lastMessage, String lastMessageTime) {
}
