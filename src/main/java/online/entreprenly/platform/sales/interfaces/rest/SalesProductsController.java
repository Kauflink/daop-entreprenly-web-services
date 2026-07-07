package online.entreprenly.platform.sales.interfaces.rest;

import online.entreprenly.platform.sales.application.internal.outboundservices.acl.ProductCatalogService;
import online.entreprenly.platform.sales.interfaces.rest.resources.SalesProductResource;
import online.entreprenly.platform.sales.interfaces.rest.transform.SalesProductResourceFromCatalogProductAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller that exposes the sellable product catalog for the point-of-sale view.
 *
 * <p>The catalog (products with price and already-computed stock) is obtained through the
 * Sales {@link ProductCatalogService} outbound port, whose ACL adapter reads it from the
 * Inventory bounded context. The point-of-sale client thus gets everything it needs in a
 * single request without knowing about products or lots separately.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/sales-products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sales Products", description = "Products available to sell at the point of sale")
public class SalesProductsController {

    private final ProductCatalogService productCatalogService;

    public SalesProductsController(ProductCatalogService productCatalogService) {
        this.productCatalogService = productCatalogService;
    }

    @GetMapping
    @Operation(
        summary = "List sellable products",
        description = "Retrieves the authenticated seller's catalog with each product's currently "
                + "available stock, computed by the Inventory context, ready to be sold.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Sellable products found")
    public ResponseEntity<List<SalesProductResource>> getSalesProducts(
            @AuthenticationPrincipal UserDetails userDetails) {
        var products = productCatalogService.findByOwner(userDetails.getUsername()).stream()
                .map(SalesProductResourceFromCatalogProductAssembler::toResourceFromCatalogProduct)
                .toList();
        return ResponseEntity.ok(products);
    }
}
