package online.entreprenly.platform.profile.domain.model.queries;

/**
 * Query to get a profile by its identifier.
 *
 * @param profileId the profile identifier
 */
public record GetProfileByIdQuery(Long profileId) {
}
