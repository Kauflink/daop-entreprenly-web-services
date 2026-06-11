package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ChatSaleService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
import online.entreprenly.platform.sales.application.commandservices.SaleCommandService;
import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentReceipt;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * ACL adapter that creates a Sale in the Sales BC when a ChatOrder is confirmed.
 *
 * <p>Chatbot orders capture the product name and price agreed in the chat but do
 * not carry an inventory product id. {@link SaleItem#of} accepts a null productId,
 * so the sale is recorded as a completed chatbot transaction without requiring the
 * seller to have a matching product entry.</p>
 */
@Service
public class SalesBcChatSaleService implements ChatSaleService {

    private static final Logger log = LoggerFactory.getLogger(SalesBcChatSaleService.class);

    private final SaleCommandService saleCommandService;

    public SalesBcChatSaleService(SaleCommandService saleCommandService) {
        this.saleCommandService = saleCommandService;
    }

    @Override
    public void createSaleForOrder(String ownerEmail, Long sellerId, ChatOrder order) {
        if (ownerEmail == null || ownerEmail.isBlank() || sellerId == null || order == null) return;
        if (order.getItems() == null || order.getItems().isEmpty()) return;

        var saleItems = order.getItems().stream()
                .map(item -> SaleItem.of(null, item.productName(), item.quantity(), null, item.unitPrice()))
                .toList();

        var paymentReceipt = new PaymentReceipt(PaymentMethod.YAPE, null, order.getTotal(), Instant.now());

        var command = new CreateSaleCommand(
                ownerEmail,
                sellerId,
                saleItems,
                PaymentMethod.YAPE,
                paymentReceipt,
                SaleStatus.COMPLETED);

        var result = saleCommandService.handle(command);
        if (result.isFailure()) {
            log.warn("[sales-acl] could not register sale for chat order {}: {}",
                    order.getOrderNumber(), result);
        } else {
            log.info("[sales-acl] sale registered for confirmed chat order {}", order.getOrderNumber());
        }
    }
}
