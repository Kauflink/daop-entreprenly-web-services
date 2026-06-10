package online.entreprenly.platform.sales.interfaces.rest;

import online.entreprenly.platform.sales.application.commandservices.CashRegisterCommandService;
import online.entreprenly.platform.sales.application.queryservices.CashRegisterQueryService;
import online.entreprenly.platform.sales.domain.model.queries.GetAllCashRegistersQuery;
import online.entreprenly.platform.sales.domain.model.queries.GetCashRegisterByDateQuery;
import online.entreprenly.platform.sales.interfaces.rest.resources.CashRegisterResource;
import online.entreprenly.platform.sales.interfaces.rest.resources.CreateCashRegisterResource;
import online.entreprenly.platform.sales.interfaces.rest.resources.UpdateCashRegisterResource;
import online.entreprenly.platform.sales.interfaces.rest.transform.CashRegisterResourceFromEntityAssembler;
import online.entreprenly.platform.sales.interfaces.rest.transform.CreateCashRegisterCommandFromResourceAssembler;
import online.entreprenly.platform.sales.interfaces.rest.transform.UpdateCashRegisterCommandFromResourceAssembler;
import online.entreprenly.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller that exposes cash register resources.
 */
@RestController
@RequestMapping(value = "/api/v1/cash-registers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Cash Registers", description = "Daily cash register endpoints")
public class CashRegistersController {

    private final CashRegisterCommandService cashRegisterCommandService;
    private final CashRegisterQueryService cashRegisterQueryService;

    public CashRegistersController(CashRegisterCommandService cashRegisterCommandService,
                                   CashRegisterQueryService cashRegisterQueryService) {
        this.cashRegisterCommandService = cashRegisterCommandService;
        this.cashRegisterQueryService = cashRegisterQueryService;
    }

    @PostMapping
    @Operation(
        summary = "Open a cash register",
        description = "Opens a cash register for a business day.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cash register opened",
                    content = @Content(schema = @Schema(implementation = CashRegisterResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "A cash register already exists for the day")
    })
    public ResponseEntity<?> createCashRegister(@Valid @RequestBody CreateCashRegisterResource resource) {
        var command = CreateCashRegisterCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), resource);
        var result = cashRegisterCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, CashRegisterResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List cash registers",
        description = "Retrieves cash registers, optionally filtered by business day.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Cash registers found")
    public ResponseEntity<List<CashRegisterResource>> getCashRegisters(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CashRegisterResource> resources;
        if (date != null) {
            resources = cashRegisterQueryService.handle(new GetCashRegisterByDateQuery(AuthenticatedUser.email(), date))
                    .map(CashRegisterResourceFromEntityAssembler::toResourceFromEntity)
                    .map(List::of)
                    .orElseGet(List::of);
        } else {
            resources = cashRegisterQueryService.handle(new GetAllCashRegistersQuery(AuthenticatedUser.email())).stream()
                    .map(CashRegisterResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
        }
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{cashRegisterId}")
    @Operation(
        summary = "Update a cash register",
        description = "Updates the running totals and sale count of a cash register.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cash register updated",
                    content = @Content(schema = @Schema(implementation = CashRegisterResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Cash register not found", content = @Content)
    })
    public ResponseEntity<?> updateCashRegister(@PathVariable Long cashRegisterId,
                                                @Valid @RequestBody UpdateCashRegisterResource resource) {
        var command = UpdateCashRegisterCommandFromResourceAssembler.toCommandFromResource(
                AuthenticatedUser.email(), cashRegisterId, resource);
        var result = cashRegisterCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, CashRegisterResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
