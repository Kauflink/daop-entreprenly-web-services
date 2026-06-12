package online.entreprenly.platform.chatbot.infrastructure.acl;

import online.entreprenly.platform.chatbot.application.internal.outboundservices.acl.ChatSaleService;
import online.entreprenly.platform.chatbot.domain.model.aggregates.ChatOrder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * ACL adapter that creates a Sale and updates the daily CashRegister in the
 * Sales BC when a ChatOrder is confirmed.
 */
@Service
public class SalesBcChatSaleService implements ChatSaleService {

    private static final Logger log = LoggerFactory.getLogger(SalesBcChatSaleService.class);

    private final SaleCommandService saleCommandService;
    private final CashRegisterCommandService cashRegisterCommandService;
    private final CashRegisterQueryService cashRegisterQueryService;

    public SalesBcChatSaleService(SaleCommandService saleCommandService,
                                  CashRegisterCommandService cashRegisterCommandService,
                                  CashRegisterQueryService cashRegisterQueryService) {
        this.saleCommandService = saleCommandService;
        this.cashRegisterCommandService = cashRegisterCommandService;
        this.cashRegisterQueryService = cashRegisterQueryService;
    }

    @Override
    public void createSaleForOrder(String ownerEmail, Long sellerId, ChatOrder order) {
        if (ownerEmail == null || ownerEmail.isBlank() || sellerId == null || order == null) return;
        if (order.getItems() == null || order.getItems().isEmpty()) return;

        var saleItems = order.getItems().stream()
                .map(item -> SaleItem.of(null, item.productName(), item.quantity(), null, item.unitPrice()))
                .toList();

        var paymentReceipt = new PaymentReceipt(PaymentMethod.YAPE, null, order.getTotal(), Instant.now());
        var saleResult = saleCommandService.handle(new CreateSaleCommand(
                ownerEmail, sellerId, saleItems, PaymentMethod.YAPE, paymentReceipt, SaleStatus.COMPLETED));

        if (saleResult.isFailure()) {
            log.warn("[sales-acl] could not register sale for chat order {}: {}",
                    order.getOrderNumber(), saleResult);
            return;
        }
        log.info("[sales-acl] sale registered for confirmed chat order {}", order.getOrderNumber());

        updateCashRegister(ownerEmail, order.getTotal());
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
