package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;

import java.time.Instant;


public record CreateChatMessageCommand(Long conversationId, String content, MessageSender sender,
                                       MessageType type, Instant sentAt) {
}
