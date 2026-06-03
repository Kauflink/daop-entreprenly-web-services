package online.entreprenly.platform.profile.application.internal.queryservices;

import online.entreprenly.platform.profile.application.queryservices.ProfileQueryService;
import online.entreprenly.platform.profile.domain.model.aggregates.Profile;
import online.entreprenly.platform.profile.domain.model.queries.GetProfileByIdQuery;
import online.entreprenly.platform.profile.domain.model.queries.GetProfileByUserIdQuery;
import online.entreprenly.platform.profile.domain.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Application service that resolves profile read queries.
 */
@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.profileId());
    }

    @Override
    public Optional<Profile> handle(GetProfileByUserIdQuery query) {
        return profileRepository.findByUserId(query.userId());
    }
}
