package online.entreprenly.platform.subscription.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Resource matching the Angular subscription dashboard contract.
 */
@Schema(name = "SubscriptionDashboardResponse", description = "Subscription dashboard used by the frontend")
public record SubscriptionDashboardResource(
        Long id,
        String defaultBillingCycle,
        DashboardPlanResource currentPlan,
        DashboardPlanResource recommendedPlan,
        List<DashboardLimitResource> limits,
        DashboardBillingSetupResource billingSetup,
        List<DashboardActivityResource> activity
) {
    public record DashboardPlanResource(
            String id,
            String name,
            String shortDescription,
            double monthlyPrice,
            double annualPrice,
            String status,
            String statusLabel,
            String badgeLabel,
            boolean recommended,
            String currentPeriodStartDate,
            String currentPeriodEndDate,
            List<DashboardPlanFeatureResource> features
    ) {
    }

    public record DashboardPlanFeatureResource(String description, boolean available) {
    }

    public record DashboardLimitResource(String id, String label, int usedValue, int maxValue) {
    }

    public record DashboardBillingSetupResource(
            String paymentMethodTitle,
            String paymentMethodDescription,
            String paymentMethodActionLabel,
            String fiscalDataTitle,
            String fiscalDataDescription,
            String fiscalDataActionLabel,
            boolean hasPaymentMethod,
            boolean hasFiscalData,
            List<DashboardPaymentMethodResource> paymentMethods,
            DashboardFiscalDataResource fiscalData
    ) {
    }

    public record DashboardPaymentMethodResource(
            String id,
            String cardBrand,
            String lastFour,
            String holderName,
            String expiryMonth,
            String expiryYear,
            boolean isDefault
    ) {
    }

    public record DashboardFiscalDataResource(
            String documentType,
            String documentNumber,
            String businessName,
            String receiptEmail,
            String fiscalAddress
    ) {
    }

    public record DashboardActivityResource(String id, String title, String detail) {
    }
}
