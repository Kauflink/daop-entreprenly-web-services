package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageSender;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.MessageType;

import java.time.Instant;

/**
 * Command to append a message to a conversation.
 *
 * @param conversationId the conversation the message belongs to
 * @param content        the message text
 * @param sender         who authored the message
 * @param type           the content type (nullable; defaults to text)
 * @param sentAt         the instant the message was sent (nullable; defaults to now)
 */
public record CreateChatMessageCommand(Long conversationId, String content, MessageSender sender,
                                       MessageType type, Instant sentAt) {
}
