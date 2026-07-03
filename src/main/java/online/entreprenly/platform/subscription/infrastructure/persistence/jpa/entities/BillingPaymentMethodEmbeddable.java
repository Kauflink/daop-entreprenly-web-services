package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable payment method stored in the billing setup's element collection.
 *
 * <p>Holds only non-sensitive card metadata; the full card number is never persisted.</p>
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class BillingPaymentMethodEmbeddable {

    @Column(name = "method_id", length = 60)
    private String methodId;

    @Column(name = "card_brand", length = 40)
    private String cardBrand;

    @Column(name = "last_four", length = 4)
    private String lastFour;

    @Column(name = "holder_name", length = 120)
    private String holderName;

    @Column(name = "expiry_month", length = 2)
    private String expiryMonth;

    @Column(name = "expiry_year", length = 4)
    private String expiryYear;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;
}
