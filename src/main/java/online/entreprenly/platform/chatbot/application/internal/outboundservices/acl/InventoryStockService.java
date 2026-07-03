package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.OrderItem;

import java.util.List;


public interface InventoryStockService {

    
    void decrementForOrder(String ownerEmail, List<OrderItem> items);
}
