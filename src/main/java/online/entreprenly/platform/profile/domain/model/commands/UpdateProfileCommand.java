package online.entreprenly.platform.profile.domain.model.commands;

/**
 * Command to update the editable fields of a profile.
 *
 * @param profileId the profile identifier
 * @param firstName the new first name
 * @param lastName  the new last name
 * @param phone     the new phone number (nullable)
 * @param avatarUrl the new avatar URL (nullable)
 */
public record UpdateProfileCommand(Long profileId, String firstName, String lastName, String phone, String avatarUrl) {
}
