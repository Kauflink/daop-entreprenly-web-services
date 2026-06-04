package online.entreprenly.platform.sales.infrastructure.persistence.jpa.embeddables;

import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Embeddable persistence representation of a sale's payment receipt.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PaymentReceiptEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "receipt_method", length = 20)
    private PaymentMethod method;

    @Column(name = "receipt_transaction_code", length = 80)
    private String transactionCode;

    @Column(name = "receipt_amount")
    private Double amount;

    @Column(name = "receipt_confirmed_at")
    private Instant confirmedAt;

    public PaymentReceiptEmbeddable(PaymentMethod method, String transactionCode, Double amount, Instant confirmedAt) {
        this.method = method;
        this.transactionCode = transactionCode;
        this.amount = amount;
        this.confirmedAt = confirmedAt;
    }
}
