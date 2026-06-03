package online.entreprenly.platform.profile.application.queryservices;

import online.entreprenly.platform.profile.domain.model.aggregates.Profile;
import online.entreprenly.platform.profile.domain.model.queries.GetProfileByIdQuery;
import online.entreprenly.platform.profile.domain.model.queries.GetProfileByUserIdQuery;

import java.util.Optional;

/**
 * Application service contract for profile read queries.
 */
public interface ProfileQueryService {
    /**
     * Handles retrieval of a profile by its identifier.
     *
     * @param query profile-id query
     * @return matching profile, if found
     */
    Optional<Profile> handle(GetProfileByIdQuery query);

    /**
     * Handles retrieval of a profile by the IAM user identifier.
     *
     * @param query user-id query
     * @return matching profile, if found
     */
    Optional<Profile> handle(GetProfileByUserIdQuery query);
}
