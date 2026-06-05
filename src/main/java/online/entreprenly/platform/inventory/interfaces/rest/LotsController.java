package online.entreprenly.platform.inventory.interfaces.rest;

import online.entreprenly.platform.inventory.application.queryservices.LotQueryService;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetLotByIdQuery;
import online.entreprenly.platform.inventory.interfaces.rest.resources.LotResource;
import online.entreprenly.platform.inventory.interfaces.rest.transform.LotResourceFromEntityAssembler;
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
 * REST controller that exposes the combined (unit + weight) lot read view.
 *
 * <p>Lots are created and mutated through their type-specific endpoints
 * ({@code /inventory-unit-lots}, {@code /inventory-weight-lots}); this controller only
 * provides a unified read-only listing.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/inventory-lots", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Inventory Lots", description = "Combined inventory lot read endpoints")
public class LotsController {

    private final LotQueryService lotQueryService;

    public LotsController(LotQueryService lotQueryService) {
        this.lotQueryService = lotQueryService;
    }

    @GetMapping
    @Operation(
        summary = "List all lots",
        description = "Retrieves every registered lot (unit and weight) in a single combined view.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Lots found")
    public ResponseEntity<List<LotResource>> getAllLots() {
        var resources = lotQueryService.handle(new GetAllLotsQuery(AuthenticatedUser.email())).stream()
                .map(LotResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{lotId}")
    @Operation(
        summary = "Get lot by ID",
        description = "Retrieves a single lot by its identifier from the combined view.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lot found",
                    content = @Content(schema = @Schema(implementation = LotResource.class))),
            @ApiResponse(responseCode = "404", description = "Lot not found")
    })
    public ResponseEntity<LotResource> getLotById(@PathVariable Long lotId) {
        return lotQueryService.handle(new GetLotByIdQuery(AuthenticatedUser.email(), lotId))
                .map(LotResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
