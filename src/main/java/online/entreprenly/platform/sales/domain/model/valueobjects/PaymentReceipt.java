package online.entreprenly.platform.sales.domain.model.valueobjects;

import java.time.Instant;

/**
 * Proof of payment attached to a sale once its payment has been confirmed.
 *
 * @param method          the payment method used
 * @param transactionCode the external transaction code (e.g. Yape/Plin operation number)
 * @param amount          the amount paid
 * @param confirmedAt     the instant the payment was confirmed
 */
public record PaymentReceipt(PaymentMethod method, String transactionCode, double amount, Instant confirmedAt) {
}
