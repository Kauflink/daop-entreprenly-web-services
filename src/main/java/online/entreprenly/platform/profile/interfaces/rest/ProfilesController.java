package online.entreprenly.platform.profile.interfaces.rest;

import online.entreprenly.platform.profile.application.commandservices.ProfileCommandService;
import online.entreprenly.platform.profile.application.queryservices.ProfileQueryService;
import online.entreprenly.platform.profile.domain.model.queries.GetProfileByUserIdQuery;
import online.entreprenly.platform.profile.interfaces.rest.resources.NotificationSettingsResource;
import online.entreprenly.platform.profile.interfaces.rest.resources.PreferencesResource;
import online.entreprenly.platform.profile.interfaces.rest.resources.ProfileResource;
import online.entreprenly.platform.profile.interfaces.rest.resources.UpdateProfileResource;
import online.entreprenly.platform.profile.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import online.entreprenly.platform.profile.interfaces.rest.transform.UpdateNotificationSettingsCommandFromResourceAssembler;
import online.entreprenly.platform.profile.interfaces.rest.transform.UpdatePreferencesCommandFromResourceAssembler;
import online.entreprenly.platform.profile.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes profile resources.
 */
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "User profile management endpoints")
public class ProfilesController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesController(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @GetMapping(params = "userId")
    @Operation(
        summary = "Get profile by user ID",
        description = "Retrieves the profile that belongs to the given IAM user.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found",
                    content = @Content(schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
    })
    public ResponseEntity<ProfileResource> getProfileByUserId(@RequestParam Long userId) {
        return profileQueryService.handle(new GetProfileByUserIdQuery(userId))
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{profileId}")
    @Operation(
        summary = "Update a profile",
        description = "Updates the editable fields (name and avatar) of a profile.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated",
                    content = @Content(schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
    })
    public ResponseEntity<?> updateProfile(@PathVariable Long profileId, @Valid @RequestBody UpdateProfileResource resource) {
        var command = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(profileId, resource);
        var result = profileCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ProfileResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PutMapping("/{profileId}/preferences")
    @Operation(
        summary = "Update preferences",
        description = "Updates the preferences of a profile.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Preferences updated",
                    content = @Content(schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
    })
    public ResponseEntity<?> updatePreferences(@PathVariable Long profileId, @Valid @RequestBody PreferencesResource resource) {
        var command = UpdatePreferencesCommandFromResourceAssembler.toCommandFromResource(profileId, resource);
        var result = profileCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ProfileResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PutMapping("/{profileId}/notification-settings")
    @Operation(
        summary = "Update notification settings",
        description = "Updates the notification settings of a profile.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification settings updated",
                    content = @Content(schema = @Schema(implementation = ProfileResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
    })
    public ResponseEntity<?> updateNotificationSettings(@PathVariable Long profileId, @Valid @RequestBody NotificationSettingsResource resource) {
        var command = UpdateNotificationSettingsCommandFromResourceAssembler.toCommandFromResource(profileId, resource);
        var result = profileCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ProfileResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
