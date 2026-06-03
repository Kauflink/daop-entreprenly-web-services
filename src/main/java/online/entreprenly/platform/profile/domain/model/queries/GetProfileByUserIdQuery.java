package online.entreprenly.platform.profile.domain.model.queries;

/**
 * Query to get a profile by the IAM user identifier it belongs to.
 *
 * @param userId the IAM user identifier
 */
public record GetProfileByUserIdQuery(Long userId) {
}
