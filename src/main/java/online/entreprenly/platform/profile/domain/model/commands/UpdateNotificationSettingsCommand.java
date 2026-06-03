package online.entreprenly.platform.profile.domain.model.commands;

/**
 * Command to update a profile's notification settings.
 *
 * @param profileId       the profile identifier
 * @param stockAlerts     whether stock alerts are enabled
 * @param paymentAlerts   whether payment alerts are enabled
 * @param chatbotMessages whether chatbot message notifications are enabled
 */
public record UpdateNotificationSettingsCommand(Long profileId, boolean stockAlerts, boolean paymentAlerts, boolean chatbotMessages) {
}
