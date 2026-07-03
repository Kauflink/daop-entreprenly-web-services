package online.entreprenly.platform.inventory.application.internal.eventhandlers;

import online.entreprenly.platform.inventory.application.commandservices.UnitLotCommandService;
import online.entreprenly.platform.inventory.application.commandservices.UnitProductCommandService;
import online.entreprenly.platform.inventory.application.commandservices.WeightLotCommandService;
import online.entreprenly.platform.inventory.application.commandservices.WeightProductCommandService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightProductCommand;
import online.entreprenly.platform.inventory.domain.repositories.UnitProductRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Seeds a small catalog of demo inventory products (sold by unit and by weight) together with
 * their initial lots for the demo account, so the application has data to showcase. Idempotent:
 * the seeding is skipped when the demo account already owns inventory products.
 */
@Service
@Slf4j
public class InventoryDemoDataSeeder {

    private static final String DEMO_OWNER_EMAIL = "test.beta@entreprenly.online";

    private final UnitProductCommandService unitProductCommandService;
    private final WeightProductCommandService weightProductCommandService;
    private final UnitLotCommandService unitLotCommandService;
    private final WeightLotCommandService weightLotCommandService;
    private final UnitProductRepository unitProductRepository;

    public InventoryDemoDataSeeder(UnitProductCommandService unitProductCommandService,
                                   WeightProductCommandService weightProductCommandService,
                                   UnitLotCommandService unitLotCommandService,
                                   WeightLotCommandService weightLotCommandService,
                                   UnitProductRepository unitProductRepository) {
        this.unitProductCommandService = unitProductCommandService;
        this.weightProductCommandService = weightProductCommandService;
        this.unitLotCommandService = unitLotCommandService;
        this.weightLotCommandService = weightLotCommandService;
        this.unitProductRepository = unitProductRepository;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        if (!unitProductRepository.findAllByOwnerEmail(DEMO_OWNER_EMAIL).isEmpty()) {
            log.info("[inventory-seeder] demo products already present for {}, skipping", DEMO_OWNER_EMAIL);
            return;
        }

        log.info("[inventory-seeder] seeding demo inventory for {}", DEMO_OWNER_EMAIL);
        var now = Instant.now();

        // === Products sold by unit (with their unit lots) ===
        seedUnitProduct("Coca-Cola 500ml", "Gaseosa en botella", "QR-UP-0001", 3.5, 500.0, "Coca-Cola",
                "QR-UL-0001", 24, now.plus(180, ChronoUnit.DAYS));
        seedUnitProduct("Galletas Oreo", "Galletas rellenas", "QR-UP-0002", 2.5, 110.0, "Oreo",
                "QR-UL-0002", 40, now.plus(120, ChronoUnit.DAYS));
        seedUnitProduct("Leche Gloria Entera", "Leche evaporada en lata", "QR-UP-0003", 4.2, 400.0, "Gloria",
                "QR-UL-0003", 30, now.plus(240, ChronoUnit.DAYS));

        // === Products sold by weight (with their weight lots) ===
        seedWeightProduct("Manzana Roja", "Manzana fresca vendida por kilo", "QR-WP-0001", 4.5,
                "QR-WL-0001", 20.0, now);
        seedWeightProduct("Platano de Seda", "Platano fresco por kilo", "QR-WP-0002", 3.0,
                "QR-WL-0002", 35.0, now);
        seedWeightProduct("Tomate", "Tomate fresco por kilo", "QR-WP-0003", 3.8,
                "QR-WL-0003", 15.0, now);

        log.info("[inventory-seeder] demo inventory seeding finished for {}", DEMO_OWNER_EMAIL);
    }

    private void seedUnitProduct(String name, String description, String productCodeQR, double price,
                                 double weightGrams, String brand, String lotCodeQR, int quantity,
                                 Instant expiryDate) {
        var productResult = unitProductCommandService.handle(new CreateUnitProductCommand(
                DEMO_OWNER_EMAIL, name, description, productCodeQR, price, weightGrams, brand));
        if (productResult instanceof Result.Success<UnitProduct, ApplicationError> success) {
            var productId = success.value().getId();
            unitLotCommandService.handle(new CreateUnitLotCommand(
                    DEMO_OWNER_EMAIL, productId, lotCodeQR, Instant.now(), quantity, expiryDate));
        } else {
            log.warn("[inventory-seeder] could not seed unit product '{}': {}", name, productResult);
        }
    }

    private void seedWeightProduct(String name, String description, String productCodeQR, double pricePerKg,
                                   String lotCodeQR, double quantityKg, Instant entryDate) {
        var productResult = weightProductCommandService.handle(new CreateWeightProductCommand(
                DEMO_OWNER_EMAIL, name, description, productCodeQR, pricePerKg));
        if (productResult instanceof Result.Success<WeightProduct, ApplicationError> success) {
            var productId = success.value().getId();
            weightLotCommandService.handle(new CreateWeightLotCommand(
                    DEMO_OWNER_EMAIL, productId, lotCodeQR, entryDate, quantityKg));
        } else {
            log.warn("[inventory-seeder] could not seed weight product '{}': {}", name, productResult);
        }
    }
}
