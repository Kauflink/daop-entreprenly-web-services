package online.entreprenly.platform.chatbot.domain.model.commands;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.ConversationStatus;

/**
 * Command to update a conversation's status and last-message projection.
 *
 * @param conversationId  the conversation identifier
 * @param status          the desired status (nullable to leave unchanged)
 * @param lastMessage     the latest message preview (nullable to leave unchanged)
 * @param lastMessageTime the latest message time label (nullable to leave unchanged)
 */
public record UpdateConversationCommand(Long conversationId, ConversationStatus status,
                                        String lastMessage, String lastMessageTime) {
}
