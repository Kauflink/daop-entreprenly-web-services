package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities;

import online.entreprenly.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA persistence entity for a user's billing setup.
 */
@Entity
@Table(name = "billing_setups")
@Getter
@Setter
@NoArgsConstructor
public class BillingSetupPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "payment_method_title", length = 120)
    private String paymentMethodTitle;

    @Column(name = "payment_method_description", length = 255)
    private String paymentMethodDescription;

    @Column(name = "payment_method_action_label", length = 120)
    private String paymentMethodActionLabel;

    @Column(name = "fiscal_data_title", length = 120)
    private String fiscalDataTitle;

    @Column(name = "fiscal_data_description", length = 255)
    private String fiscalDataDescription;

    @Column(name = "fiscal_data_action_label", length = 120)
    private String fiscalDataActionLabel;

    @Column(name = "has_payment_method", nullable = false)
    private boolean hasPaymentMethod;

    @Column(name = "has_fiscal_data", nullable = false)
    private boolean hasFiscalData;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "billing_payment_methods", joinColumns = @JoinColumn(name = "billing_setup_id"))
    private List<BillingPaymentMethodEmbeddable> paymentMethods = new ArrayList<>();

    @Embedded
    private FiscalDataEmbeddable fiscalData;
}
