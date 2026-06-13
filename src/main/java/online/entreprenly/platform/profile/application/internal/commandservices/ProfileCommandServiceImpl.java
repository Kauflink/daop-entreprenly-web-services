package online.entreprenly.platform.profile.application.internal.commandservices;

import online.entreprenly.platform.profile.application.commandservices.ProfileCommandService;
import online.entreprenly.platform.profile.domain.model.aggregates.Profile;
import online.entreprenly.platform.profile.domain.model.commands.CreateProfileCommand;
import online.entreprenly.platform.profile.domain.model.commands.UpdateNotificationSettingsCommand;
import online.entreprenly.platform.profile.domain.model.commands.UpdatePreferencesCommand;
import online.entreprenly.platform.profile.domain.model.commands.UpdateProfileCommand;
import online.entreprenly.platform.profile.domain.model.commands.UpdateProfilePlanCommand;
import online.entreprenly.platform.profile.domain.model.valueobjects.NotificationSettings;
import online.entreprenly.platform.profile.domain.model.valueobjects.Preferences;
import online.entreprenly.platform.profile.domain.repositories.ProfileRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Profile command service implementation.
 */
@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Result<Profile, ApplicationError> handle(CreateProfileCommand command) {
        if (profileRepository.existsByUserId(command.userId())) {
            return Result.failure(ApplicationError.conflict("Profile", "A profile already exists for this user"));
        }
        var profile = new Profile(command.userId(), command.firstName(), command.lastName(),
                command.role(), command.plan(), command.phone(), command.timezone());
        return Result.success(profileRepository.save(profile));
    }

    @Override
    public Result<Profile, ApplicationError> handle(UpdateProfileCommand command) {
        return profileRepository.findById(command.profileId())
                .<Result<Profile, ApplicationError>>map(profile -> {
                    profile.updateProfile(command.firstName(), command.lastName(), command.phone(), command.avatarUrl());
                    return Result.success(profileRepository.save(profile));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("Profile", String.valueOf(command.profileId()))));
    }

    @Override
    public Result<Profile, ApplicationError> handle(UpdatePreferencesCommand command) {
        return profileRepository.findById(command.profileId())
                .<Result<Profile, ApplicationError>>map(profile -> {
                    profile.updatePreferences(new Preferences(
                            command.language(), command.timezone(), command.theme(), command.currency()));
                    return Result.success(profileRepository.save(profile));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("Profile", String.valueOf(command.profileId()))));
    }

    @Override
    public Result<Profile, ApplicationError> handle(UpdateNotificationSettingsCommand command) {
        return profileRepository.findById(command.profileId())
                .<Result<Profile, ApplicationError>>map(profile -> {
                    profile.updateNotificationSettings(new NotificationSettings(
                            command.stockAlerts(), command.paymentAlerts(), command.chatbotMessages()));
                    return Result.success(profileRepository.save(profile));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("Profile", String.valueOf(command.profileId()))));
    }

    @Override
    public Result<Profile, ApplicationError> handle(UpdateProfilePlanCommand command) {
        return profileRepository.findByUserId(command.userId())
                .<Result<Profile, ApplicationError>>map(profile -> {
                    profile.changePlan(command.plan());
                    return Result.success(profileRepository.save(profile));
                })
                .orElseGet(() -> Result.failure(ApplicationError.notFound("Profile", "userId=" + command.userId())));
    }
}
