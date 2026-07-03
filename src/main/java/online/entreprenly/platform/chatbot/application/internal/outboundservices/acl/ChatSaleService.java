package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;

public interface ChatSaleService {

    void createSaleForOrder(String ownerEmail, Long sellerId, ChatOrder order);
}
