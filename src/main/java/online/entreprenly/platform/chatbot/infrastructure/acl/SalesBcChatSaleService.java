package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ChatSaleService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.sales.interfaces.acl.ChatSaleLine;
import online.entreprenly.platform.sales.interfaces.acl.SalesContextFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ACL adapter that registers a confirmed {@link ChatOrder} as a sale in the Sales bounded
 * context through its ACL facade, translating chatbot order lines into the sales contract.
 */
@Service
public class SalesBcChatSaleService implements ChatSaleService {

    private static final Logger log = LoggerFactory.getLogger(SalesBcChatSaleService.class);

    private final SalesContextFacade salesContextFacade;

    public SalesBcChatSaleService(SalesContextFacade salesContextFacade) {
        this.salesContextFacade = salesContextFacade;
    }

    @Override
    public void createSaleForOrder(String ownerEmail, Long sellerId, ChatOrder order) {
        if (ownerEmail == null || ownerEmail.isBlank() || sellerId == null || order == null) return;
        if (order.getItems() == null || order.getItems().isEmpty()) return;

        var lines = order.getItems().stream()
                .map(item -> new ChatSaleLine(item.productName(), item.quantity(), item.unitPrice()))
                .toList();

        var registered = salesContextFacade.registerChatSale(ownerEmail, sellerId, lines, order.getTotal());
        if (registered) {
            log.info("[sales-acl] sale registered for confirmed chat order {}", order.getOrderNumber());
        } else {
            log.warn("[sales-acl] could not register sale for chat order {}", order.getOrderNumber());
        }
    }
}
