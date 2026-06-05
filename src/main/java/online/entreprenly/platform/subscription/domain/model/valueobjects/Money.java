package online.entreprenly.platform.subscription.domain.model.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Monetary value used by subscription plans and payments.
 */
public record Money(BigDecimal amount, String currency) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        currency = currency.trim().toUpperCase();
    }
}
