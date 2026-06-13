package online.entreprenly.platform.subscription.domain.model.valueobjects;

/**
 * A stored payment method for a user's billing setup.
 *
 * <p>Only non-sensitive card metadata is kept: the brand, the last four digits, the holder name
 * and the expiry. The full card number (PAN) is never stored.</p>
 *
 * @param methodId    client-side identifier of the method (e.g. {@code payment-method-1})
 * @param cardBrand   card brand label (e.g. {@code Visa})
 * @param lastFour    last four digits of the card
 * @param holderName  card holder name
 * @param expiryMonth two-digit expiry month
 * @param expiryYear  two-digit expiry year
 * @param isDefault   whether this is the default method
 */
public record PaymentMethod(String methodId, String cardBrand, String lastFour, String holderName,
                            String expiryMonth, String expiryYear, boolean isDefault) {
}
