package online.entreprenly.platform.chatbot.application.internal.outboundservices.acl;

import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;

/**
 * Outbound port that registers a confirmed chat order as a sale in the Sales BC.
 */
public interface ChatSaleService {

    void createSaleForOrder(String ownerEmail, Long sellerId, ChatOrder order);
}
