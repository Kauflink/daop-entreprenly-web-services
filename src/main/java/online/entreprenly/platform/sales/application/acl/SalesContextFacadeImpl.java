package online.entreprenly.platform.sales.application.acl;

import online.entreprenly.platform.sales.application.commandservices.CashRegisterCommandService;
import online.entreprenly.platform.sales.application.commandservices.SaleCommandService;
import online.entreprenly.platform.sales.application.queryservices.CashRegisterQueryService;
import online.entreprenly.platform.sales.domain.model.commands.CreateCashRegisterCommand;
import online.entreprenly.platform.sales.domain.model.commands.CreateSaleCommand;
import online.entreprenly.platform.sales.domain.model.commands.UpdateCashRegisterCommand;
import online.entreprenly.platform.sales.domain.model.queries.GetCashRegisterByDateQuery;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Application-layer implementation of the Sales ACL facade.
 *
 * <p>Registers a sale and updates the daily cash register, exposing the operation to other
 * bounded contexts without coupling them to the Sales internal model.</p>
 */
@Service
public class SalesContextFacadeImpl implements SalesContextFacade {

    private static final Logger log = LoggerFactory.getLogger(SalesContextFacadeImpl.class);

    private final SaleCommandService saleCommandService;
    private final CashRegisterCommandService cashRegisterCommandService;
    private final CashRegisterQueryService cashRegisterQueryService;

    public SalesContextFacadeImpl(SaleCommandService saleCommandService,
                                  CashRegisterCommandService cashRegisterCommandService,
                                  CashRegisterQueryService cashRegisterQueryService) {
        this.saleCommandService = saleCommandService;
        this.cashRegisterCommandService = cashRegisterCommandService;
        this.cashRegisterQueryService = cashRegisterQueryService;
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

        updateCashRegister(ownerEmail, total);
        return true;
    }

    private void updateCashRegister(String ownerEmail, double amount) {
        var today = LocalDate.now(ZoneId.of("America/Lima"));
        var existing = cashRegisterQueryService.handle(new GetCashRegisterByDateQuery(ownerEmail, today));

        if (existing.isPresent()) {
            var reg = existing.get();
            cashRegisterCommandService.handle(new UpdateCashRegisterCommand(
                    ownerEmail,
                    reg.getId(),
                    reg.getTotalCash(),
                    Math.round((reg.getTotalDigital() + amount) * 100.0) / 100.0,
                    reg.getSaleCount() + 1));
        } else {
            cashRegisterCommandService.handle(new CreateCashRegisterCommand(
                    ownerEmail, today, 0.0, amount));
        }
    }
}
