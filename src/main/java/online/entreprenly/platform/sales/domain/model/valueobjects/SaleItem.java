package online.entreprenly.platform.sales.domain.model.valueobjects;

/**
 * Line item of a sale. A sale item is sold either by unit ({@code quantity}) or by
 * weight ({@code weightKg}); the {@code subtotal} is derived from whichever applies.
 *
 * @param productId   the product identifier this line refers to
 * @param productName the product display name captured at sale time
 * @param quantity    the number of units sold (nullable when sold by weight)
 * @param weightKg    the weight sold in kilograms (nullable when sold by unit)
 * @param unitPrice   the price per unit or per kilogram
 * @param subtotal    the computed line subtotal
 */
public record SaleItem(Long productId, String productName, Integer quantity, Double weightKg,
                       double unitPrice, double subtotal) {

    /**
     * Creates a sale item computing its subtotal from the provided pricing data.
     *
     * @param productId   the product identifier
     * @param productName the product display name
     * @param quantity    the number of units (nullable when sold by weight)
     * @param weightKg    the weight in kilograms (nullable when sold by unit)
     * @param unitPrice   the price per unit or per kilogram
     * @return a sale item with its subtotal computed
     */
    public static SaleItem of(Long productId, String productName, Integer quantity, Double weightKg, double unitPrice) {
        return new SaleItem(productId, productName, quantity, weightKg, unitPrice,
                computeSubtotal(quantity, weightKg, unitPrice));
    }

    /**
     * Computes the subtotal for a line, favouring weight-based pricing when present.
     *
     * @param quantity  the number of units (nullable)
     * @param weightKg  the weight in kilograms (nullable)
     * @param unitPrice the price per unit or per kilogram
     * @return the rounded subtotal
     */
    public static double computeSubtotal(Integer quantity, Double weightKg, double unitPrice) {
        double raw;
        if (weightKg != null) {
            raw = unitPrice * weightKg;
        } else if (quantity != null) {
            raw = unitPrice * quantity;
        } else {
            raw = 0d;
        }
        return Math.round(raw * 100.0) / 100.0;
    }
}
