package online.entreprenly.platform.subscription.domain.model.valueobjects;

/**
 * Fiscal/billing data used to issue receipts for a subscription.
 *
 * @param documentType   document type (e.g. {@code RUC}, {@code DNI})
 * @param documentNumber document number
 * @param businessName   business or legal name
 * @param receiptEmail   email where receipts are sent
 * @param fiscalAddress  fiscal address
 */
public record FiscalData(String documentType, String documentNumber, String businessName,
                         String receiptEmail, String fiscalAddress) {
}
