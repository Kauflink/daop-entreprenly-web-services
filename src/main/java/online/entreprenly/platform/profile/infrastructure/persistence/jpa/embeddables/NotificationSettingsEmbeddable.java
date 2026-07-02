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

    public NotificationSettingsEmbeddable(boolean stockAlerts) {
        this.stockAlerts = stockAlerts;
    }
}
