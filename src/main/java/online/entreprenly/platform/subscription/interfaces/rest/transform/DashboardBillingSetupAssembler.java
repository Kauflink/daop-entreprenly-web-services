package online.entreprenly.platform.subscription.interfaces.rest.transform;

import online.entreprenly.platform.subscription.domain.model.aggregates.BillingSetup;
import online.entreprenly.platform.subscription.domain.model.valueobjects.FiscalData;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.subscription.interfaces.rest.resources.SubscriptionDashboardResource;

import java.util.List;

/**
 * Maps between the {@link BillingSetup} aggregate and the dashboard billing-setup resource the
 * Angular client exchanges.
 */
public final class DashboardBillingSetupAssembler {

    private DashboardBillingSetupAssembler() {
    }

    public static BillingSetup toDomain(Long userId,
                                        SubscriptionDashboardResource.DashboardBillingSetupResource resource) {
        if (resource == null) {
            return null;
        }
        var paymentMethods = (resource.paymentMethods() == null ? List.<SubscriptionDashboardResource.DashboardPaymentMethodResource>of() : resource.paymentMethods())
                .stream()
                .map(item -> new PaymentMethod(item.id(), item.cardBrand(), item.lastFour(), item.holderName(),
                        item.expiryMonth(), item.expiryYear(), item.isDefault()))
                .toList();
        var fiscalData = resource.fiscalData() == null
                ? null
                : new FiscalData(
                        resource.fiscalData().documentType(),
                        resource.fiscalData().documentNumber(),
                        resource.fiscalData().businessName(),
                        resource.fiscalData().receiptEmail(),
                        resource.fiscalData().fiscalAddress());
        return new BillingSetup(
                userId,
                resource.paymentMethodTitle(),
                resource.paymentMethodDescription(),
                resource.paymentMethodActionLabel(),
                resource.fiscalDataTitle(),
                resource.fiscalDataDescription(),
                resource.fiscalDataActionLabel(),
                resource.hasPaymentMethod(),
                resource.hasFiscalData(),
                paymentMethods,
                fiscalData);
    }

    public static SubscriptionDashboardResource.DashboardBillingSetupResource toResource(BillingSetup billingSetup) {
        var paymentMethods = billingSetup.getPaymentMethods().stream()
                .map(item -> new SubscriptionDashboardResource.DashboardPaymentMethodResource(
                        item.methodId(), item.cardBrand(), item.lastFour(), item.holderName(),
                        item.expiryMonth(), item.expiryYear(), item.isDefault()))
                .toList();
        var fiscalData = billingSetup.getFiscalData() == null
                ? null
                : new SubscriptionDashboardResource.DashboardFiscalDataResource(
                        billingSetup.getFiscalData().documentType(),
                        billingSetup.getFiscalData().documentNumber(),
                        billingSetup.getFiscalData().businessName(),
                        billingSetup.getFiscalData().receiptEmail(),
                        billingSetup.getFiscalData().fiscalAddress());
        return new SubscriptionDashboardResource.DashboardBillingSetupResource(
                billingSetup.getPaymentMethodTitle(),
                billingSetup.getPaymentMethodDescription(),
                billingSetup.getPaymentMethodActionLabel(),
                billingSetup.getFiscalDataTitle(),
                billingSetup.getFiscalDataDescription(),
                billingSetup.getFiscalDataActionLabel(),
                billingSetup.isHasPaymentMethod(),
                billingSetup.isHasFiscalData(),
                paymentMethods,
                fiscalData);
    }
}
