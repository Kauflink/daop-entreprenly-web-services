package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.assemblers;

import online.entreprenly.platform.subscription.domain.model.aggregates.BillingSetup;
import online.entreprenly.platform.subscription.domain.model.valueobjects.FiscalData;
import online.entreprenly.platform.subscription.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.BillingPaymentMethodEmbeddable;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.BillingSetupPersistenceEntity;
import online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities.FiscalDataEmbeddable;

import java.util.ArrayList;
import java.util.List;

/**
 * Static assembler between billing setup domain and persistence representations.
 */
public final class BillingSetupPersistenceAssembler {

    private BillingSetupPersistenceAssembler() {
    }

    public static BillingSetup toDomainFromPersistence(BillingSetupPersistenceEntity entity) {
        if (entity == null) return null;
        var billingSetup = new BillingSetup();
        billingSetup.restoreState(
                entity.getId(),
                entity.getUserId(),
                entity.getPaymentMethodTitle(),
                entity.getPaymentMethodDescription(),
                entity.getPaymentMethodActionLabel(),
                entity.getFiscalDataTitle(),
                entity.getFiscalDataDescription(),
                entity.getFiscalDataActionLabel(),
                entity.isHasPaymentMethod(),
                entity.isHasFiscalData(),
                toDomainPaymentMethods(entity.getPaymentMethods()),
                toDomainFiscalData(entity.getFiscalData()));
        return billingSetup;
    }

    public static BillingSetupPersistenceEntity toPersistenceFromDomain(BillingSetup billingSetup) {
        if (billingSetup == null) return null;
        var entity = new BillingSetupPersistenceEntity();
        if (billingSetup.getId() != null) {
            entity.setId(billingSetup.getId());
        }
        entity.setUserId(billingSetup.getUserId());
        entity.setPaymentMethodTitle(billingSetup.getPaymentMethodTitle());
        entity.setPaymentMethodDescription(billingSetup.getPaymentMethodDescription());
        entity.setPaymentMethodActionLabel(billingSetup.getPaymentMethodActionLabel());
        entity.setFiscalDataTitle(billingSetup.getFiscalDataTitle());
        entity.setFiscalDataDescription(billingSetup.getFiscalDataDescription());
        entity.setFiscalDataActionLabel(billingSetup.getFiscalDataActionLabel());
        entity.setHasPaymentMethod(billingSetup.isHasPaymentMethod());
        entity.setHasFiscalData(billingSetup.isHasFiscalData());
        entity.setPaymentMethods(toPersistencePaymentMethods(billingSetup.getPaymentMethods()));
        entity.setFiscalData(toPersistenceFiscalData(billingSetup.getFiscalData()));
        return entity;
    }

    private static List<PaymentMethod> toDomainPaymentMethods(List<BillingPaymentMethodEmbeddable> embeddables) {
        var result = new ArrayList<PaymentMethod>();
        if (embeddables == null) return result;
        for (var embeddable : embeddables) {
            result.add(new PaymentMethod(
                    embeddable.getMethodId(),
                    embeddable.getCardBrand(),
                    embeddable.getLastFour(),
                    embeddable.getHolderName(),
                    embeddable.getExpiryMonth(),
                    embeddable.getExpiryYear(),
                    embeddable.isDefault()));
        }
        return result;
    }

    private static List<BillingPaymentMethodEmbeddable> toPersistencePaymentMethods(List<PaymentMethod> paymentMethods) {
        var result = new ArrayList<BillingPaymentMethodEmbeddable>();
        if (paymentMethods == null) return result;
        for (var paymentMethod : paymentMethods) {
            var embeddable = new BillingPaymentMethodEmbeddable();
            embeddable.setMethodId(paymentMethod.methodId());
            embeddable.setCardBrand(paymentMethod.cardBrand());
            embeddable.setLastFour(paymentMethod.lastFour());
            embeddable.setHolderName(paymentMethod.holderName());
            embeddable.setExpiryMonth(paymentMethod.expiryMonth());
            embeddable.setExpiryYear(paymentMethod.expiryYear());
            embeddable.setDefault(paymentMethod.isDefault());
            result.add(embeddable);
        }
        return result;
    }

    private static FiscalData toDomainFiscalData(FiscalDataEmbeddable embeddable) {
        if (embeddable == null || isBlank(embeddable.getDocumentNumber())) {
            return null;
        }
        return new FiscalData(
                embeddable.getDocumentType(),
                embeddable.getDocumentNumber(),
                embeddable.getBusinessName(),
                embeddable.getReceiptEmail(),
                embeddable.getFiscalAddress());
    }

    private static FiscalDataEmbeddable toPersistenceFiscalData(FiscalData fiscalData) {
        if (fiscalData == null) {
            return null;
        }
        var embeddable = new FiscalDataEmbeddable();
        embeddable.setDocumentType(fiscalData.documentType());
        embeddable.setDocumentNumber(fiscalData.documentNumber());
        embeddable.setBusinessName(fiscalData.businessName());
        embeddable.setReceiptEmail(fiscalData.receiptEmail());
        embeddable.setFiscalAddress(fiscalData.fiscalAddress());
        return embeddable;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
