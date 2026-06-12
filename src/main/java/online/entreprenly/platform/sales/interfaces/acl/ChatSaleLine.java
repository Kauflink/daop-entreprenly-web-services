package online.entreprenly.platform.sales.interfaces.acl;

/**
 * A line of a sale originated in the chatbot, used by other bounded contexts through the
 * {@link SalesContextFacade}.
 *
 * @param productName the product display name agreed in the chat
 * @param quantity    the number of units sold
 * @param unitPrice   the price per unit at sale time
 */
public record ChatSaleLine(String productName, int quantity, double unitPrice) {
}
