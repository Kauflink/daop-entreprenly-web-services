package online.entreprenly.platform.inventory.interfaces.rest;

import online.entreprenly.platform.inventory.application.commandservices.WeightLotCommandService;
import online.entreprenly.platform.inventory.application.queryservices.WeightLotQueryService;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteWeightLotCommand;
import online.entreprenly.platform.inventory.domain.model.queries.GetAllWeightLotsQuery;
import online.entreprenly.platform.inventory.domain.model.queries.GetWeightLotByIdQuery;
import online.entreprenly.platform.inventory.interfaces.rest.resources.CreateWeightLotResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.UpdateWeightLotResource;
import online.entreprenly.platform.inventory.interfaces.rest.resources.WeightLotResource;
import online.entreprenly.platform.inventory.interfaces.rest.transform.CreateWeightLotCommandFromResourceAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.UpdateWeightLotCommandFromResourceAssembler;
import online.entreprenly.platform.inventory.interfaces.rest.transform.WeightLotResourceFromEntityAssembler;
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
 * REST controller that exposes weight lot resources for the authenticated account.
 */
@RestController
@RequestMapping(value = "/api/v1/inventory-weight-lots", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Inventory Weight Lots", description = "By-weight inventory lot endpoints")
public class WeightLotsController {

    private final WeightLotCommandService weightLotCommandService;
    private final WeightLotQueryService weightLotQueryService;

    public WeightLotsController(WeightLotCommandService weightLotCommandService,
                               WeightLotQueryService weightLotQueryService) {
        this.weightLotCommandService = weightLotCommandService;
        this.weightLotQueryService = weightLotQueryService;
    }

    @PostMapping
    @Operation(
        summary = "Create a weight lot",
        description = "Registers a new stock batch for a by-weight product owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Weight lot created",
                    content = @Content(schema = @Schema(implementation = WeightLotResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Referenced weight product not found", content = @Content)
    })
    public ResponseEntity<?> createWeightLot(@AuthenticationPrincipal UserDetails userDetails,
                                             @Valid @RequestBody CreateWeightLotResource resource) {
        var command = CreateWeightLotCommandFromResourceAssembler.toCommandFromResource(
                userDetails.getUsername(), resource);
        var result = weightLotCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, WeightLotResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List weight lots",
        description = "Retrieves every weight lot owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Weight lots found")
    public ResponseEntity<List<WeightLotResource>> getAllWeightLots(
            @AuthenticationPrincipal UserDetails userDetails) {
        var resources = weightLotQueryService.handle(new GetAllWeightLotsQuery(userDetails.getUsername())).stream()
                .map(WeightLotResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{weightLotId}")
    @Operation(
        summary = "Get weight lot by ID",
        description = "Retrieves a weight lot owned by the authenticated account by its identifier.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Weight lot found",
                    content = @Content(schema = @Schema(implementation = WeightLotResource.class))),
            @ApiResponse(responseCode = "404", description = "Weight lot not found", content = @Content)
    })
    public ResponseEntity<WeightLotResource> getWeightLotById(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable Long weightLotId) {
        return weightLotQueryService.handle(new GetWeightLotByIdQuery(userDetails.getUsername(), weightLotId))
                .map(WeightLotResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{weightLotId}")
    @Operation(
        summary = "Update a weight lot",
        description = "Updates a weight lot owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Weight lot updated",
                    content = @Content(schema = @Schema(implementation = WeightLotResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Weight lot not found", content = @Content)
    })
    public ResponseEntity<?> updateWeightLot(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long weightLotId,
                                             @Valid @RequestBody UpdateWeightLotResource resource) {
        var command = UpdateWeightLotCommandFromResourceAssembler.toCommandFromResource(
                userDetails.getUsername(), weightLotId, resource);
        var result = weightLotCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, WeightLotResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{weightLotId}")
    @Operation(
        summary = "Delete a weight lot",
        description = "Deletes a weight lot owned by the authenticated account.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Weight lot deleted"),
            @ApiResponse(responseCode = "404", description = "Weight lot not found", content = @Content)
    })
    public ResponseEntity<?> deleteWeightLot(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long weightLotId) {
        var result = weightLotCommandService.handle(new DeleteWeightLotCommand(userDetails.getUsername(), weightLotId));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, id -> null, HttpStatus.NO_CONTENT);
    }
}
