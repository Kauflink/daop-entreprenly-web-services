package online.entreprenly.platform.inventory.domain.model.valueobjects;

/**
 * Kind of stock alert raised for a product or lot.
 *
 * <p>Each type carries the lowercase wire {@link #getValue() value} exposed to clients
 * and a {@link #getPriority() priority} used to order alerts from most to least urgent.</p>
 */
public enum AlertType {
    EXPIRED("expired", 1),
    OUT_OF_STOCK("out_of_stock", 2),
    EXPIRING_SOON("expiring_soon", 3),
    LOW_STOCK("low_stock", 4);

    private final String value;
    private final int priority;

    AlertType(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    /**
     * Returns the lowercase wire value of this alert type.
     *
     * @return the alert type code
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the urgency priority of this alert type (lower is more urgent).
     *
     * @return the alert priority
     */
    public int getPriority() {
        return priority;
    }
}
