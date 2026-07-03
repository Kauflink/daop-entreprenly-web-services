package online.entreprenly.platform.subscription.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable fiscal data for the billing setup entity.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class FiscalDataEmbeddable {

    @Column(name = "fiscal_document_type", length = 30)
    private String documentType;

    @Column(name = "fiscal_document_number", length = 30)
    private String documentNumber;

    @Column(name = "fiscal_business_name", length = 160)
    private String businessName;

    @Column(name = "fiscal_receipt_email", length = 160)
    private String receiptEmail;

    @Column(name = "fiscal_address", length = 255)
    private String fiscalAddress;
}
