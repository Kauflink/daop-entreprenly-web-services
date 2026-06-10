package online.entreprenly.platform.inventory.interfaces.rest;

import online.entreprenly.platform.inventory.application.commandservices.WeightProductCommandService;
import online.entreprenly.platform.inventory.application.queryservices.WeightProductQueryService;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteWeightProductCommand;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightProductsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetWeightProductByIdQuery;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateWeightProductResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateWeightProductResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.WeightProductResource;
import online.entreprenly.platform.inventory.interfaces.rest.transform.CreateWeightProductCommandFromResourceAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.UpdateWeightProductCommandFromResourceAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.WeightProductResourceFromEntityAssembler;
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
 * REST controller that exposes weight product resources for the authenticated account.
 */
@RestController
@RequestMapping(value = "/api/v1/inventory-weight-products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Inventory Weight Products", description = "By-weight inventory product endpoints")
public class WeightProductsController {

    private final WeightProductCommandService weightProductCommandService;
    private final WeightProductQueryService weightProductQueryService;

    public WeightProductsController(WeightProductCommandService weightProductCommandService,
                                    WeightProductQueryService weightProductQueryService) {
        this.weightProductCommandService = weightProductCommandService;
        this.weightProductQueryService = weightProductQueryService;
    }

    @PostMapping
    @Operation(
        summary = "Create a weight product",
        description = "Registers a new product tracked and sold by weight, owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Weight product created",
                    content = @Content(schema = @Schema(implementation = WeightProductResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "A weight product already exists with the QR code")
    })
    public ResponseEntity<?> createWeightProduct(@Valid @RequestBody CreateWeightProductResource resource) {
        var command = CreateWeightProductCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), resource);
        var result = weightProductCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, WeightProductResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List weight products",
        description = "Retrieves every weight product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Weight products found")
    public ResponseEntity<List<WeightProductResource>> getAllWeightProducts() {
        var resources = weightProductQueryService.handle(new GetAllWeightProductsQuery(AuthenticatedUser.email()))
                .stream()
                .map(WeightProductResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{weightProductId}")
    @Operation(
        summary = "Get weight product by ID",
        description = "Retrieves a weight product owned by the authenticated account by its identifier.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Weight product found",
                    content = @Content(schema = @Schema(implementation = WeightProductResource.class))),
            @ApiResponse(responseCode = "404", description = "Weight product not found", content = @Content)
    })
    public ResponseEntity<WeightProductResource> getWeightProductById(@PathVariable Long weightProductId) {
        return weightProductQueryService.handle(new GetWeightProductByIdQuery(AuthenticatedUser.email(), weightProductId))
                .map(WeightProductResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{weightProductId}")
    @Operation(
        summary = "Update a weight product",
        description = "Updates a weight product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Weight product updated",
                    content = @Content(schema = @Schema(implementation = WeightProductResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Weight product not found", content = @Content)
    })
    public ResponseEntity<?> updateWeightProduct(@PathVariable Long weightProductId,
                                                 @Valid @RequestBody UpdateWeightProductResource resource) {
        var command = UpdateWeightProductCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), weightProductId, resource);
        var result = weightProductCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, WeightProductResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{weightProductId}")
    @Operation(
        summary = "Delete a weight product",
        description = "Deletes a weight product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Weight product deleted"),
            @ApiResponse(responseCode = "404", description = "Weight product not found", content = @Content)
    })
    public ResponseEntity<?> deleteWeightProduct(@PathVariable Long weightProductId) {
        var result = weightProductCommandService.handle(
                new DeleteWeightProductCommand(AuthenticatedUser.email(), weightProductId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, id -> null, HttpStatus.NO_CONTENT);
    }
}
