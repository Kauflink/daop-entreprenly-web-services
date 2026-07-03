package online.entreprenly.platform.chatbot.domain.model.valueobjects;


public record CatalogProduct(String name, double price, boolean soldByWeight, double availableStock) {

    
    public boolean isInStock() {
        return availableStock > 0;
    }
}
