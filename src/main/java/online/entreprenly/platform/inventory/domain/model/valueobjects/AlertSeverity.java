package online.entreprenly.platform.inventory.domain.model.valueobjects;

/**
 * Severity assigned to a stock alert.
 */
public enum AlertSeverity {
    WARNING("warning"),
    CRITICAL("critical");

    private final String value;

    AlertSeverity(String value) {
        this.value = value;
    }

    /**
     * Returns the lowercase wire value of this severity.
     *
     * @return the severity code
     */
    public String getValue() {
        return value;
    }
}
