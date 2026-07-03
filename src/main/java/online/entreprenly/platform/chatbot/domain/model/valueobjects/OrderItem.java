package online.entreprenly.platform.chatbot.domain.model.valueobjects;


public record OrderItem(String productName, int quantity, double unitPrice) {

    
    public double subtotal() {
        return Math.round(unitPrice * quantity * 100.0) / 100.0;
    }
}
