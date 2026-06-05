package online.entreprenly.platform.inventory.interfaces.rest;

import online.entreprenly.platform.inventory.application.queryservices.StockAlertQueryService;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllStockAlertsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetStockAlertByIdQuery;
import online.entreprenly.platform.inventory.interfaces.rest.resources.StockAlertResource;
import online.entreprenly.platform.inventory.interfaces.rest.transform.StockAlertResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller that exposes derived stock alerts.
 *
 * <p>Stock alerts are computed on demand from the current inventory products and lots,
 * so this controller only offers read operations.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/inventory-stock-alerts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Inventory Stock Alerts", description = "Derived inventory stock alert endpoints")
public class StockAlertsController {

    private final StockAlertQueryService stockAlertQueryService;

    public StockAlertsController(StockAlertQueryService stockAlertQueryService) {
        this.stockAlertQueryService = stockAlertQueryService;
    }

    @GetMapping
    @Operation(
        summary = "List stock alerts",
        description = "Computes and retrieves every currently raised stock alert, ordered by descending urgency.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Stock alerts computed")
    public ResponseEntity<List<StockAlertResource>> getAllStockAlerts() {
        var resources = stockAlertQueryService.handle(new GetAllStockAlertsQuery()).stream()
                .map(StockAlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{stockAlertId}")
    @Operation(
        summary = "Get stock alert by ID",
        description = "Retrieves a single currently raised stock alert by its run-scoped identifier.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock alert found",
                    content = @Content(schema = @Schema(implementation = StockAlertResource.class))),
            @ApiResponse(responseCode = "404", description = "Stock alert not found")
    })
    public ResponseEntity<StockAlertResource> getStockAlertById(@PathVariable Long stockAlertId) {
        return stockAlertQueryService.handle(new GetStockAlertByIdQuery(stockAlertId))
                .map(StockAlertResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
