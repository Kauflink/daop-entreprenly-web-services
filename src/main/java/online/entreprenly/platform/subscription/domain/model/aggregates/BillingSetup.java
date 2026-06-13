package online.entreprenly.platform.subscription.domain.model.aggregates;

import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import online.entreprenly.platform.subscription.domain.model.valueobjects.FiscalData;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Billing setup aggregate root: the payment methods and fiscal data a user keeps for their
 * subscription. There is at most one billing setup per user ({@code userId}).
 *
 * <p>This aggregate mirrors the billing section of the Angular subscription dashboard contract,
 * including the display labels the client maintains, so the dashboard survives restarts.</p>
 */
@Getter
public class BillingSetup extends AbstractDomainAggregateRoot<BillingSetup> {

    @Setter
    private Long id;
    private Long userId;
    private String paymentMethodTitle;
    private String paymentMethodDescription;
    private String paymentMethodActionLabel;
    private String fiscalDataTitle;
    private String fiscalDataDescription;
    private String fiscalDataActionLabel;
    private boolean hasPaymentMethod;
    private boolean hasFiscalData;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private FiscalData fiscalData;

    public BillingSetup() {
    }

    public BillingSetup(Long userId, String paymentMethodTitle, String paymentMethodDescription,
                        String paymentMethodActionLabel, String fiscalDataTitle, String fiscalDataDescription,
                        String fiscalDataActionLabel, boolean hasPaymentMethod, boolean hasFiscalData,
                        List<PaymentMethod> paymentMethods, FiscalData fiscalData) {
        this.userId = userId;
        this.paymentMethodTitle = paymentMethodTitle;
        this.paymentMethodDescription = paymentMethodDescription;
        this.paymentMethodActionLabel = paymentMethodActionLabel;
        this.fiscalDataTitle = fiscalDataTitle;
        this.fiscalDataDescription = fiscalDataDescription;
        this.fiscalDataActionLabel = fiscalDataActionLabel;
        this.hasPaymentMethod = hasPaymentMethod;
        this.hasFiscalData = hasFiscalData;
        this.paymentMethods = paymentMethods == null ? new ArrayList<>() : new ArrayList<>(paymentMethods);
        this.fiscalData = fiscalData;
    }

    public void restoreState(Long id, Long userId, String paymentMethodTitle, String paymentMethodDescription,
                             String paymentMethodActionLabel, String fiscalDataTitle, String fiscalDataDescription,
                             String fiscalDataActionLabel, boolean hasPaymentMethod, boolean hasFiscalData,
                             List<PaymentMethod> paymentMethods, FiscalData fiscalData) {
        this.id = id;
        this.userId = userId;
        this.paymentMethodTitle = paymentMethodTitle;
        this.paymentMethodDescription = paymentMethodDescription;
        this.paymentMethodActionLabel = paymentMethodActionLabel;
        this.fiscalDataTitle = fiscalDataTitle;
        this.fiscalDataDescription = fiscalDataDescription;
        this.fiscalDataActionLabel = fiscalDataActionLabel;
        this.hasPaymentMethod = hasPaymentMethod;
        this.hasFiscalData = hasFiscalData;
        this.paymentMethods = paymentMethods == null ? new ArrayList<>() : new ArrayList<>(paymentMethods);
        this.fiscalData = fiscalData;
    }
}
