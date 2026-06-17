package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import online.entreprenly.platform.chatbot.domain.model.valueobjects.CatalogProduct;

import java.util.List;


public interface ProductCatalogService {

    
    List<CatalogProduct> findByOwner(String ownerEmail);
}
