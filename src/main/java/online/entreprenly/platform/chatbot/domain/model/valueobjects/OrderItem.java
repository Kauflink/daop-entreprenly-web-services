package online.entreprenly.platform.chatbot.domain.model.valueobjects;

/**
 * Line item of a {@code ChatOrder}. Captures the product name and pricing as
 * agreed during the conversation, decoupled from any inventory identity so the
 * order remains a faithful record of what the client requested.
 *
 * @param productName the product display name agreed in the chat
 * @param quantity    the number of units requested
 * @param unitPrice   the price per unit at order time
 */
public record OrderItem(String productName, int quantity, double unitPrice) {

    /**
     * @return the line subtotal, rounded to two decimals
     */
    public double subtotal() {
        return Math.round(unitPrice * quantity * 100.0) / 100.0;
    }
}
