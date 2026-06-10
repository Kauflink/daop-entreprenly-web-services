package online.entreprenly.platform.inventory.interfaces.rest;

import online.entreprenly.platform.inventory.application.commandservices.UnitProductCommandService;
import online.entreprenly.platform.inventory.application.queryservices.UnitProductQueryService;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetUnitProductByIdQuery;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateUnitProductResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UnitProductResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateUnitProductResource;
import online.entreprenly.platform.inventory.interfaces.rest.transform.CreateUnitProductCommandFromResourceAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.UnitProductResourceFromEntityAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.UpdateUnitProductCommandFromResourceAssembler;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller that exposes unit product resources for the authenticated account.
 */
@RestController
@RequestMapping(value = "/api/v1/inventory-unit-products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Inventory Unit Products", description = "Per-unit inventory product endpoints")
public class UnitProductsController {

    private final UnitProductCommandService unitProductCommandService;
    private final UnitProductQueryService unitProductQueryService;

    public UnitProductsController(UnitProductCommandService unitProductCommandService,
                                  UnitProductQueryService unitProductQueryService) {
        this.unitProductCommandService = unitProductCommandService;
        this.unitProductQueryService = unitProductQueryService;
    }

    @PostMapping
    @Operation(
        summary = "Create a unit product",
        description = "Registers a new product tracked and sold per unit, owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unit product created",
                    content = @Content(schema = @Schema(implementation = UnitProductResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "A unit product already exists with the QR code")
    })
    public ResponseEntity<?> createUnitProduct(@Valid @RequestBody CreateUnitProductResource resource) {
        var command = CreateUnitProductCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), resource);
        var result = unitProductCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UnitProductResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List unit products",
        description = "Retrieves every unit product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Unit products found")
    public ResponseEntity<List<UnitProductResource>> getAllUnitProducts() {
        var resources = unitProductQueryService.handle(new GetAllUnitProductsQuery(AuthenticatedUser.email())).stream()
                .map(UnitProductResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{unitProductId}")
    @Operation(
        summary = "Get unit product by ID",
        description = "Retrieves a unit product owned by the authenticated account by its identifier.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit product found",
                    content = @Content(schema = @Schema(implementation = UnitProductResource.class))),
            @ApiResponse(responseCode = "404", description = "Unit product not found", content = @Content)
    })
    public ResponseEntity<UnitProductResource> getUnitProductById(@PathVariable Long unitProductId) {
        return unitProductQueryService.handle(new GetUnitProductByIdQuery(AuthenticatedUser.email(), unitProductId))
                .map(UnitProductResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{unitProductId}")
    @Operation(
        summary = "Update a unit product",
        description = "Updates a unit product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit product updated",
                    content = @Content(schema = @Schema(implementation = UnitProductResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Unit product not found", content = @Content)
    })
    public ResponseEntity<?> updateUnitProduct(@PathVariable Long unitProductId,
                                               @Valid @RequestBody UpdateUnitProductResource resource) {
        var command = UpdateUnitProductCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), unitProductId, resource);
        var result = unitProductCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UnitProductResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{unitProductId}")
    @Operation(
        summary = "Delete a unit product",
        description = "Deletes a unit product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unit product deleted"),
            @ApiResponse(responseCode = "404", description = "Unit product not found", content = @Content)
    })
    public ResponseEntity<?> deleteUnitProduct(@PathVariable Long unitProductId) {
        var result = unitProductCommandService.handle(
                new DeleteUnitProductCommand(AuthenticatedUser.email(), unitProductId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, id -> null, HttpStatus.NO_CONTENT);
    }
}
