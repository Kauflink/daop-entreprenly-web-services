package online.entreprenly.platform.profile.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable persistence representation of the user's notification settings.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class NotificationSettingsEmbeddable {

    @Column(name = "notify_stock_alerts", nullable = false)
    private boolean stockAlerts;

    @Column(name = "notify_payment_alerts", nullable = false)
    private boolean paymentAlerts;

    @Column(name = "notify_chatbot_messages", nullable = false)
    private boolean chatbotMessages;

    public NotificationSettingsEmbeddable(boolean stockAlerts, boolean paymentAlerts, boolean chatbotMessages) {
        this.stockAlerts = stockAlerts;
        this.paymentAlerts = paymentAlerts;
        this.chatbotMessages = chatbotMessages;
    }
}
