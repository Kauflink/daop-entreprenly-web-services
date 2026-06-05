package online.entreprenly.platform.inventory.interfaces.rest;

import online.entreprenly.platform.inventory.application.commandservices.UnitLotCommandService;
import online.entreprenly.platform.inventory.application.queryservices.UnitLotQueryService;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteUnitLotCommand;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllUnitLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetUnitLotByIdQuery;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateUnitLotResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UnitLotResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateUnitLotResource;
import online.entreprenly.platform.inventory.interfaces.rest.transform.CreateUnitLotCommandFromResourceAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.UnitLotResourceFromEntityAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.UpdateUnitLotCommandFromResourceAssembler;
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
 * REST controller that exposes unit lot resources for the authenticated account.
 */
@RestController
@RequestMapping(value = "/api/v1/inventory-unit-lots", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Inventory Unit Lots", description = "Per-unit inventory lot endpoints")
public class UnitLotsController {

    private final UnitLotCommandService unitLotCommandService;
    private final UnitLotQueryService unitLotQueryService;

    public UnitLotsController(UnitLotCommandService unitLotCommandService,
                             UnitLotQueryService unitLotQueryService) {
        this.unitLotCommandService = unitLotCommandService;
        this.unitLotQueryService = unitLotQueryService;
    }

    @PostMapping
    @Operation(
        summary = "Create a unit lot",
        description = "Registers a new stock batch for a per-unit product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unit lot created",
                    content = @Content(schema = @Schema(implementation = UnitLotResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Referenced unit product not found")
    })
    public ResponseEntity<?> createUnitLot(@Valid @RequestBody CreateUnitLotResource resource) {
        var command = CreateUnitLotCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), resource);
        var result = unitLotCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UnitLotResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List unit lots",
        description = "Retrieves every unit lot owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Unit lots found")
    public ResponseEntity<List<UnitLotResource>> getAllUnitLots() {
        var resources = unitLotQueryService.handle(new GetAllUnitLotsQuery(AuthenticatedUser.email())).stream()
                .map(UnitLotResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{unitLotId}")
    @Operation(
        summary = "Get unit lot by ID",
        description = "Retrieves a unit lot owned by the authenticated account by its identifier.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit lot found",
                    content = @Content(schema = @Schema(implementation = UnitLotResource.class))),
            @ApiResponse(responseCode = "404", description = "Unit lot not found")
    })
    public ResponseEntity<UnitLotResource> getUnitLotById(@PathVariable Long unitLotId) {
        return unitLotQueryService.handle(new GetUnitLotByIdQuery(AuthenticatedUser.email(), unitLotId))
                .map(UnitLotResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{unitLotId}")
    @Operation(
        summary = "Update a unit lot",
        description = "Updates a unit lot owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit lot updated",
                    content = @Content(schema = @Schema(implementation = UnitLotResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Unit lot not found")
    })
    public ResponseEntity<?> updateUnitLot(@PathVariable Long unitLotId,
                                           @Valid @RequestBody UpdateUnitLotResource resource) {
        var command = UpdateUnitLotCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), unitLotId, resource);
        var result = unitLotCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UnitLotResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{unitLotId}")
    @Operation(
        summary = "Delete a unit lot",
        description = "Deletes a unit lot owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unit lot deleted"),
            @ApiResponse(responseCode = "404", description = "Unit lot not found")
    })
    public ResponseEntity<?> deleteUnitLot(@PathVariable Long unitLotId) {
        var result = unitLotCommandService.handle(new DeleteUnitLotCommand(AuthenticatedUser.email(), unitLotId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, id -> null, HttpStatus.NO_CONTENT);
    }
}
