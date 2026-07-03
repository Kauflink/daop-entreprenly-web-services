package online.entreprenly.platform.sales.application.acl;

import online.entreprenly.platform.sales.application.commandservices.SaleCommandService;
import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentMethod;
import online.entreprenly.platform.sales.domain.model.valueobjects.PaymentReceipt;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleItem;
import online.entreprenly.platform.sales.domain.model.valueobjects.SaleStatus;
import online.entreprenly.platform.sales.interfaces.acl.ChatSaleLine;
import online.entreprenly.platform.sales.interfaces.acl.SalesContextFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Application-layer implementation of the Sales ACL facade.
 *
 * <p>Registers a sale coming from another bounded context (e.g. a confirmed chatbot order)
 * without coupling the caller to the Sales internal model. The day takings are derived from
 * the registered sales, so no separate cash-register update is needed here.</p>
 */
@Service
public class SalesContextFacadeImpl implements SalesContextFacade {

    private static final Logger log = LoggerFactory.getLogger(SalesContextFacadeImpl.class);

    private final SaleCommandService saleCommandService;

    public SalesContextFacadeImpl(SaleCommandService saleCommandService) {
        this.saleCommandService = saleCommandService;
    }

    @Override
    public boolean registerChatSale(String ownerEmail, Long sellerId, List<ChatSaleLine> lines, double total) {
        if (ownerEmail == null || ownerEmail.isBlank() || sellerId == null || lines == null || lines.isEmpty()) {
            return false;
        }

        // productId=0 is a chatbot-order sentinel: sale_items.product_id is NOT NULL
        // but chatbot orders don't have a catalog product id, only a name.
        var saleItems = lines.stream()
                .map(line -> SaleItem.of(0L, line.productName(), line.quantity(), null, line.unitPrice()))
                .toList();

        var paymentReceipt = new PaymentReceipt(PaymentMethod.YAPE, null, total, Instant.now());
        var saleResult = saleCommandService.handle(new CreateSaleCommand(
                ownerEmail, sellerId, saleItems, PaymentMethod.YAPE, paymentReceipt, SaleStatus.COMPLETED));

        if (saleResult.isFailure()) {
            log.warn("[sales-acl] could not register sale: {}", saleResult);
            return false;
        }

        return true;
    }
}
