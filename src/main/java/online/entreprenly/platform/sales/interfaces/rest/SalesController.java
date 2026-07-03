package online.entreprenly.platform.sales.interfaces.rest;

import online.entreprenly.platform.sales.application.commandservices.SaleCommandService;
import online.entreprenly.platform.sales.application.queryservices.SaleQueryService;
import online.entreprenly.platform.sales.domain.model.queries.GetAllSalesQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetSaleByIdQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetSalesByDateQuery;
import online.entreprenly.platform.sales.interfaces.rest.resources.CreateSaleResource;
import online.entreprenly.platform.sales.interfaces.rest.resources.SaleResource;
import online.entreprenly.platform.sales.interfaces.rest.transform.CreateSaleCommandFromResourceAssembler;
import online.entreprenly.platform.sales.interfaces.rest.transform.SaleResourceFromEntityAssembler;
import online.entreprenly.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller that exposes sale resources.
 */
@RestController
@RequestMapping(value = "/api/v1/sales", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sales", description = "Point-of-sale transaction endpoints")
public class SalesController {

    private final SaleCommandService saleCommandService;
    private final SaleQueryService saleQueryService;

    public SalesController(SaleCommandService saleCommandService, SaleQueryService saleQueryService) {
        this.saleCommandService = saleCommandService;
        this.saleQueryService = saleQueryService;
    }

    @PostMapping
    @Operation(
        summary = "Register a sale",
        description = "Registers a new point-of-sale transaction. The server recomputes the total and line subtotals.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale registered",
                    content = @Content(schema = @Schema(implementation = SaleResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> createSale(@AuthenticationPrincipal UserDetails userDetails,
                                        @Valid @RequestBody CreateSaleResource resource) {
        var command = CreateSaleCommandFromResourceAssembler.toCommandFromResource(
                userDetails.getUsername(), resource);
        var result = saleCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SaleResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List sales",
        description = "Retrieves the registered sales. When the optional 'date' parameter is provided, only the sales of that business day are returned.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Sales found")
    public ResponseEntity<List<SaleResource>> getAllSales(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var owner = userDetails.getUsername();
        var sales = (date != null
                ? saleQueryService.handle(new GetSalesByDateQuery(owner, date))
                : saleQueryService.handle(new GetAllSalesQuery(owner)))
                .stream()
                .map(SaleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{saleId}")
    @Operation(
        summary = "Get sale by ID",
        description = "Retrieves a sale by its unique identifier.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale found",
                    content = @Content(schema = @Schema(implementation = SaleResource.class))),
            @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
    })
    public ResponseEntity<SaleResource> getSaleById(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable Long saleId) {
        return saleQueryService.handle(new GetSaleByIdQuery(userDetails.getUsername(), saleId))
                .map(SaleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
