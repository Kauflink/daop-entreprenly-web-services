package online.entreprenly.platform.chatbot.domain.services;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;
import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;

import java.util.List;
import java.util.Optional;


public interface ProductReplyComposer {

    
    Optional<String> compose(String incomingContent, List<CatalogProduct> catalog);

    
    Optional<OrderItem> detectOrder(String incomingContent, List<CatalogProduct> catalog);

    
    Optional<OrderItem> detectOrder(String incomingContent, List<CatalogProduct> catalog, CatalogProduct contextProduct);

    
    Optional<CatalogProduct> matchProduct(String incomingContent, List<CatalogProduct> catalog);
}
